from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator
from airflow.contrib.operators.spark_submit_operator import SparkSubmitOperator
from airflow.utils.dates import days_ago


with DAG(dag_id="CarFine", start_date=days_ago(1), schedule_interval='*/5 * * * *',
         catchup=False) as dag:

    downloadTask = BashOperator(
        task_id="get_file",
        bash_command="wget -P /home/truongvq/airflow-data https://www.dl.dropboxusercontent.com/s/q1de0kkhhxp5vo9/data.json",
    )

    showFile = BashOperator(
        task_id="show_file",
        bash_command="cd /home/truongvq/airflow-data && ls"
    )

    spark_task = SparkSubmitOperator(
        task_id="spark-save-db",
        conn_id="spark-connection",
        java_class="com.viettel.truongvq.spark.SparkJob",
        application="scala/big-data-fundamentals/target/big-data-fundamentals.jar",
        driver_class_path="airflow-data/postgresql-42.6.0.jar",
        jars="airflow-data/postgresql-42.6.0.jar",
        conf={"spark.executorEnv.location":"/airflow-data/data.json"},
        verbose=True,)

downloadTask >> showFile >> spark_task