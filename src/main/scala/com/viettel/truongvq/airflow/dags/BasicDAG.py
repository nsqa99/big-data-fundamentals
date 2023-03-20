from airflow import DAG
from datetime import datetime
from airflow.operators.python_operator import PythonOperator

def message():
    print("First DAG executed Successfully!!")

with DAG(dag_id="FirstDAG", start_date=datetime(2023,3,20), schedule_interval="@hourly",
         catchup=False) as dag:

    task = PythonOperator(
        task_id="task",
        python_callable=message)

task