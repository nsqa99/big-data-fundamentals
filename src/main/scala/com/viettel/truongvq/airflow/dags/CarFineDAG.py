from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator
from airflow.contrib.operators.spark_submit_operator import SparkSubmitOperator
from airflow.operators.python import BranchPythonOperator
from airflow.utils.dates import days_ago
from airflow.operators.python import PythonOperator
from airflow.contrib.sensors.file_sensor import FileSensor
from airflow.operators.postgres_operator import PostgresOperator
from airflow.hooks.postgres_hook import PostgresHook


default_args = {
    "owner": "airflow",}



with DAG(dag_id="CarFine", start_date=days_ago(1), schedule_interval="*/2 * * * *",
         default_args=default_args, catchup=False, tags=["truong"]) as dag:

    def branch_function(**kwargs):
        print(kwargs)
        day_of_month = kwargs["ti"].xcom_pull(task_ids="get_day_of_month", key="day")
        print(day_of_month)
        if day_of_month == 24:
            return "get_data_and_push_notification_task"
        else:
            return "get_data_task"
            

    def day_of_month(**kwargs):
        kwargs["ti"].xcom_push('day', datetime.today().day) 

    def get_car_fine_info():
        sql = "SELECT car.phone_number, car.customer, count(1) AS number_of_car_fine FROM car_fine JOIN car ON car_fine.license_plate = car.license_plate WHERE status = 'Chưa xử phạt' GROUP BY car.phone_number, car.customer"
        pg_hook = PostgresHook(
            postgres_conn_id='postgres_connection',
            schema='postgres'
        )
        pg_conn = pg_hook.get_conn()
        cursor = pg_conn.cursor()
        cursor.execute(sql_stmt)
        return cursor.fetchall()

    getDateTask = PythonOperator(
        task_id="get_day_of_month",
        python_callable=day_of_month,
    )

    branchTask = BranchPythonOperator(
        task_id="branch_task",
        python_callable=branch_function,
    )
    
    getDataTask = BashOperator(
        task_id="get_data_task",
        bash_command="echo start get data",
    )

    getDataAndPushNotificationTask = BashOperator(
        task_id="get_data_and_push_notification_task",
        bash_command="echo start get data and push notification"
    )

    downloadTask1 = BashOperator(
        task_id="get_file1",
        bash_command="wget -P /home/truongvq/airflow-data/download https://dl.dropboxusercontent.com/s/dmjmhyjk9p7yhgq/car-fine-data-short.json",
    )

    downloadTask2 = BashOperator(
        task_id="get_file2",
        bash_command="wget -P /home/truongvq/airflow-data/download https://dl.dropboxusercontent.com/s/dmjmhyjk9p7yhgq/car-fine-data-short.json",
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
        verbose=True,)
    
    deleteFileTask = BashOperator(
        task_id="delete_file",
        bash_command="rm /home/truongvq/airflow-data/download/car-fine-data-short.json"
    )

    getCarFineTask = PythonOperator(
        task_id='get_car_fine',
        python_callable=get_car_fine_info,
        do_xcom_push=True
    )
    

getDateTask >> branchTask >> [getDataTask, getDataAndPushNotificationTask]
getDataTask >> downloadTask1 >> saveCarFineToDBTask >> deleteFileTask
getDataAndPushNotificationTask >> downloadTask2 >> saveCarFineToDBTask >> deleteFileTask
downloadTask2 >> getCarFineTask