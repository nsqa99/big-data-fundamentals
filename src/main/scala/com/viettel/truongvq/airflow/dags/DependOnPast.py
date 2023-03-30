from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator

with DAG(dag_id="DependOnPast", start_date=datetime(2023, 3, 27), schedule_interval="*/1 * * * *", max_active_runs=1, tags=['truongvq']) as dag:

    firstTask = BashOperator(
        task_id="first_task",
        bash_command="sleep 20s",
        depends_on_past=True
    )

firstTask