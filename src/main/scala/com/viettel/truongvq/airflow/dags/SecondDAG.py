from datetime import datetime

from airflow.decorators import dag, task

@dag(dag_id="SecondDAG", start_date=datetime(2023, 3, 21), schedule_interval="@hourly", catchup=False)
def secondDAG():
    @task()
    def print_date():
        print(datetime)

    print_date()
secondDAG()
