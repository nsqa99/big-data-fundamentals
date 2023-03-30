from airflow.models import DAG
from airflow.providers.apache.spark.operators.spark_submit import SparkSubmitOperator
from datetime import datetime
from airflow.utils.dates import days_ago

args = {
    'owner': 'truongvq',
}

with DAG(
        dag_id='spark_examples',
        default_args=args,
        schedule_interval='@hourly',
        start_date=days_ago(1),
) as dag:
    spark_task = SparkSubmitOperator(
        task_id="spark-task",
        conn_id='spark-connection',
        java_class='com.viettel.truongvq.spark.Spark',
        application='/opt/airflow/plugins/big-data-fundamentals.jar',
        verbose=True,)

spark_task