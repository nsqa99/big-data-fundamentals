import pendulum
from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator

with DAG(dag_id="ThirdDAG", start_date=datetime(2023, 3, 21), schedule_interval="@hourly",
         catchup=False) as dag:

    task = BashOperator(
        task_id="print_date",
        bash_command="date",)

task