import time
from datetime import datetime, timedelta

from SendSMSHook import SendSMSHook
from airflow import DAG
from airflow.contrib.operators.spark_submit_operator import SparkSubmitOperator
from airflow.hooks.postgres_hook import PostgresHook
from airflow.models import Variable
from airflow.operators.bash import BashOperator
from airflow.operators.python import BranchPythonOperator
from airflow.operators.python import PythonOperator
from airflow.sensors.time_sensor import TimeSensor

default_args = {
    "owner": "airflow",
    "retries": 1,
    "retry_delay": timedelta(seconds=2),
}

with DAG(dag_id="CarFineWithCustomHook", start_date=datetime(2023, 3, 20), schedule_interval="*/1 * * * *", max_active_runs=1,
         default_args=default_args, catchup=False, tags=["truongvq"]) as dag:

    def branch_by_day_of_month_function():
        day_of_month = datetime.today().day
        print(day_of_month)
        if day_of_month == 30:
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
        car_fines = cursor.fetchall()
        print(car_fines)
        kwargs["task_instance"].xcom_push("car_fines", car_fines)
        Variable.set(key="car_fines", value=car_fines, serialize_json=True)


    def send_notification(**kwargs):
        car_fines = kwargs["task_instance"].xcom_pull(task_ids="get_car_fine_info_from_db", key="car_fines")
        print(car_fines)
        print("Send notification to {} user".format(len(car_fines)))
        # time.sleep(15)

    def send_sms(**kwargs):
        car_fine_info = kwargs.get("car_fine")
        print(car_fine)
        send_sms_hook = SendSMSHook(
            conn_id="send_sms_connection",
        )
        print(send_sms_hook)
        send_sms_hook.get_conn()
        send_sms_hook.send_car_fine_sms(car_fine_info)

    # sensorTask = FileSensor(
    #     task_id="sensor_file",
    #     poke_interval=10,
    #     timeout=50,
    #     mode="reschedule",
    #     filepath="/home/truongvq/airflow-data/download/car-fine-data-short.json"
    # )

    wait_to_send_notification = TimeSensor(
        task_id="wait_to_send_notification",
        target_time=(datetime.now() + timedelta(seconds=1)).time(),
        poke_interval=10,
    )

    branch_by_day_of_month = BranchPythonOperator(
        task_id="branch_by_day_of_month",
        python_callable=branch_by_day_of_month_function,
    )

    get_file_for_update_db = BashOperator(
        task_id="get_file_and_update_db",
        bash_command="wget -P /home/truongvq/airflow-data/download https://datasecurity.viettel.vn/s/DQNXjicuDKdyNwZ/download && cd /home/truongvq/airflow-data/download && mv download car-fine-data-short.json",
    )

    get_file_and_push_notification = BashOperator(
        task_id="get_file_and_push_notification",
        bash_command="wget -P /home/truongvq/airflow-data/download https://datasecurity.viettel.vn/s/DQNXjicuDKdyNwZ/download && cd /home/truongvq/airflow-data/download && mv download car-fine-data-short.json",
    )

    save_car_fine_to_db_task = SparkSubmitOperator(
        task_id="save_car_fine_info_to_db",
        conn_id="spark_connection",
        java_class="com.viettel.truongvq.spark.ReadDataAndInsertDB",
        application="scala/big-data-fundamentals/target/big-data-fundamentals.jar",
        driver_class_path="airflow-data/postgresql-42.6.0.jar",
        jars="airflow-data/postgresql-42.6.0.jar",
        trigger_rule="one_success",
        conf={"spark.executorEnv.location": "/home/truongvq/airflow-data/download/car-fine-data-short.json"},
        verbose=True,
    )

    delete_file_task = BashOperator(
        task_id="delete_file",
        bash_command="rm /home/truongvq/airflow-data/download/car-fine-data-short.json"
    )

    get_car_fine_info_task = PythonOperator(
        task_id="get_car_fine_info_from_db",
        python_callable=get_car_fine_info,
    )

    send_notification_task = PythonOperator(
        task_id="send_notification",
        python_callable=send_notification,
    )

    complete_task = BashOperator(
        task_id="complete_task",
        bash_command="echo complete",
    )

    car_fine_list = Variable.get(key="car_fines", default_var=[('0333151726', 'truongvq', 2), ('03331517261', 'truongvq1', 2), ('03331517262', 'truongvq2', 6)], deserialize_json=True)
    # car_fine_list = [('0333151726', 'truongvq', 2), ('03331517261', 'truongvq1', 2), ('03331517262', 'truongvq2', 6)]
    for car_fine in car_fine_list:
        task = PythonOperator(
            task_id="send_notification_to_{}".format(car_fine[0]),
            python_callable=send_sms,
            op_kwargs={"car_fine": car_fine},
        )
        send_notification_task >> task >> complete_task

branch_by_day_of_month >> [get_file_for_update_db, get_file_and_push_notification]
get_file_for_update_db >> save_car_fine_to_db_task >> delete_file_task
get_file_and_push_notification >> save_car_fine_to_db_task >> delete_file_task
get_file_and_push_notification >> get_car_fine_info_task
save_car_fine_to_db_task >> get_car_fine_info_task
wait_to_send_notification >> send_notification_task
get_car_fine_info_task >> send_notification_task
