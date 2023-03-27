from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator
from airflow.operators.latest_only import LatestOnlyOperator

with DAG(dag_id="LatestOnlyExample", start_date=datetime(2023, 3, 27), schedule_interval="@hourly", tags=['truongvq']) as dag:

    firstTask = BashOperator(
        task_id="first_task",
        bash_command="echo 1",)

    latestOnlyTask = LatestOnlyOperator(
        task_id="latest_only",
    )

    secondTask = BashOperator(
        task_id="second_task",
        bash_command="echo 2",)

firstTask
latestOnlyTask >> secondTask