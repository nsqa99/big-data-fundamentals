from airflow import DAG
from datetime import datetime
from airflow.operators.python import PythonOperator
from airflow.operators.latest_only import LatestOnlyOperator

with DAG(dag_id="MonthlySummary", start_date=datetime(2023, 1, 1), schedule_interval="0 0 2 * *", tags=['truongvq'], catchup=True) as dag:

    def send_sms_function(**kwargs):
        logical_date = kwargs["logical_date"]
        print("Send sms summary monthly data of {}/{}".format(logical_date.month, logical_date.year))

    def summary(**kwargs):
        logical_date = kwargs["logical_date"]
        print("Summary monthly data of {}/{}".format(logical_date.month, logical_date.year))

    summary_data = PythonOperator(
        task_id="summary_monthly_data",
        python_callable=summary,
    )

    latest_only_task = LatestOnlyOperator(
        task_id="latest_only",
    )

    send_sms = PythonOperator(
        task_id="send_sms_monthly_summary",
        python_callable=send_sms_function,
    )

summary_data >> send_sms
latest_only_task >> send_sms
