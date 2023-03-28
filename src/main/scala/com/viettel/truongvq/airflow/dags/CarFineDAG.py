from datetime import datetime, timedelta

from SendCarFineNotificationOperator import SendCarFineNotificationOperator
from airflow import DAG
from airflow.contrib.operators.spark_submit_operator import SparkSubmitOperator
from airflow.contrib.sensors.file_sensor import FileSensor
from airflow.hooks.postgres_hook import PostgresHook
from airflow.models import Variable
from airflow.operators.bash import BashOperator
from airflow.operators.python import BranchPythonOperator
from airflow.operators.python import PythonOperator
from airflow.utils.dates import days_ago
from airflow.utils.task_group import TaskGroup
from airflow import settings
from airflow.models import Connection


default_args = {
    "owner": "airflow",
    'retries': 1,
    'retry_delay': timedelta(seconds=2),
}

with DAG(dag_id="CarFine", start_date=datetime(2023, 3, 20), schedule_interval="*/1 * * * *", max_active_runs=1,
         default_args=default_args, catchup=False, tags=["truongvq"]) as dag:

    def day_of_month(**kwargs):
        kwargs["task_instance"].xcom_push("day", datetime.today().day) 

    def branch_function(**kwargs):
        day_of_month = kwargs["task_instance"].xcom_pull(task_ids="get_day_of_month", key="day")
        print(day_of_month)
        if day_of_month == 28:
            return "get_file_and_push_notification"
        else:
            return "get_file_and_update_db"
    

    def get_car_fine_info(**kwargs):
        # conn = Connection(
        #     conn_id="postgres_connection",
        #     conn_type="postgres",
        #     host="localhost",
        #     login="airflow",
        #     password="airflow",
        #     port=5432
        # )
        # session = settings.Session()
        # session.add(conn)
        # session.commit()

        sql = "SELECT car.phone_number, car.customer, count(1) AS number_of_car_fine FROM car_fine JOIN car ON car_fine.license_plate = car.license_plate WHERE status = 'Chưa xử phạt' GROUP BY car.phone_number, car.customer"
        pg_hook = PostgresHook(
            postgres_conn_id="postgres_connection",
            schema="postgres"
        )
        pg_conn = pg_hook.get_conn()
        cursor = pg_conn.cursor()
        cursor.execute(sql)
        car_fine = cursor.fetchall()
        print(car_fine)
        kwargs["task_instance"].xcom_push("car_fine", car_fine)
    
    def send_notification(**kwargs):
        car_fines = kwargs["task_instance"].xcom_pull(task_ids="get_car_fine_from_db", key="car_fine")        
        print(car_fines)
        Variable.set(key="car_fines", value=car_fines, serialize_json=True)
    
    car_fine_list = Variable.get(key="car_fines", default_var=[['03331517263', 'truongvq3', 24], ['03331517261', 'truongvq1', 72], ['0333151726', 'truongvq', 24]], deserialize_json=True)
    # car_fine_list = []
    with TaskGroup("send_notification_task_group", prefix_group_id=False,) as sendNotificationTasksGroup:
        for car_fine in car_fine_list:
            task = SendCarFineNotificationOperator(
                task_id="send_notification_to_{}".format(car_fine[0]),
                car_fine=car_fine,
            )

    sensorTask = FileSensor(
        task_id= "sensor_file", 
        poke_interval=10,
        timeout=50,
        mode="reschedule",
        filepath= "/home/truongvq/airflow-data/download/car-fine-data-short.json"
    )

    getDateTask = PythonOperator(
        task_id="get_day_of_month",
        provide_context=True,
        python_callable=day_of_month,
    )

    branchTask = BranchPythonOperator(
        task_id="branch_task",
        python_callable=branch_function,
    )

    downloadTask1 = BashOperator(
        task_id="get_file_and_update_db",
        bash_command="wget -P /home/truongvq/airflow-data/download https://datasecurity.viettel.vn/s/DQNXjicuDKdyNwZ/download && cd /home/truongvq/airflow-data/download && mv download car-fine-data-short.json",
    )

    downloadTask2 = BashOperator(
        task_id="get_file_and_push_notification",
        bash_command="wget -P /home/truongvq/airflow-data/download https://datasecurity.viettel.vn/s/DQNXjicuDKdyNwZ/download && cd /home/truongvq/airflow-data/download && mv download car-fine-data-short.json",
    )

    saveCarFineToDBTask = SparkSubmitOperator(
        task_id="save_car_fine_to_db",
        conn_id="spark-connection",
        java_class="com.viettel.truongvq.spark.ReadDataAndInsertDB",
        application="scala/big-data-fundamentals/target/big-data-fundamentals.jar",
        driver_class_path="airflow-data/postgresql-42.6.0.jar",
        jars="airflow-data/postgresql-42.6.0.jar",
        trigger_rule="one_success",
        conf={"spark.executorEnv.location":"/home/truongvq/airflow-data/download/car-fine-data-short.json"},
        verbose=True,
    )
    
    deleteFileTask = BashOperator(
        task_id="delete_file",
        bash_command="rm /home/truongvq/airflow-data/download/car-fine-data-short.json"
    )

    getCarFineTask = PythonOperator(
        task_id="get_car_fine_from_db",
        python_callable=get_car_fine_info,
        trigger_rule="none_skipped",
    )

    sendNotificationTask = PythonOperator(
        task_id="send_notification",
        python_callable=send_notification,
    )

    completeTask = BashOperator(
        task_id="complete_task",
        bash_command="echo complete",
    )

getDateTask >> branchTask >> [downloadTask1, downloadTask2]
downloadTask1 >> saveCarFineToDBTask >> deleteFileTask
downloadTask2 >> saveCarFineToDBTask >> deleteFileTask
[downloadTask2, saveCarFineToDBTask] >> getCarFineTask >> sendNotificationTask
sendNotificationTask >> sendNotificationTasksGroup >> completeTask
sensorTask >> deleteFileTask

    