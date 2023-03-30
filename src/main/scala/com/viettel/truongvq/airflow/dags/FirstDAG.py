from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator

with DAG(dag_id="FirstDAG", start_date=datetime(2023, 3, 20), schedule_interval="*/1 * * * *",
         tags=['truongvq']) as dag:
    firstTask = BashOperator(
        task_id="first_task",
        bash_command="",
    )

    secondTask = BashOperator(
        task_id="second_task",
        bash_command="sleep 10",
        depends_on_past=True
    )

firstTask >> secondTask
