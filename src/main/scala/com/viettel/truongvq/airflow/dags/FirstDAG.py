from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator

with DAG(dag_id="FirstDAG", start_date=datetime(2023, 3, 27), schedule_interval="@hourly", tags=['truongvq']) as dag:

    firstTask = BashOperator(
        task_id="first_task",
        bash_command="echo 1",)

    secondTask = BashOperator(
        task_id="second_task",
        bash_command="echo 2",)

firstTask >> secondTask