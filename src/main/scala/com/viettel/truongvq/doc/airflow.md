# Apache Airflow
Apache Airflow is an open-source platform for developing, scheduling, and monitoring batch-oriented workflows.

The main characteristic of Airflow workflows is that all workflows are defined in Python code. “Workflows as code” serves several purposes:

- Dynamic: Airflow pipelines are configured as Python code, allowing for dynamic pipeline generation.

- Extensible: The Airflow framework contains operators to connect with numerous technologies. All Airflow components are extensible to easily adjust to your environment.

- Flexible: Workflow parameterization is built-in leveraging the Jinja templating engine.

# Fundamental Concept

## DAG file (Directed Acyclic Graph)
A configuration file specifying the DAG structure as code.
The Python script purpose is to define DAG object

```
from airflow import DAG
from datetime import datetime
from airflow.operators.bash import BashOperator

with DAG(dag_id="FirstDAG", start_date=datetime(2023, 3, 20), schedule_interval="@hourly",
         catchup=False) as dag:

    task = BashOperator(
        task_id="print_date",
        bash_command="date",)

task
```

## Operators
Using operators helps to visualize task dependencies in our DAG code.

All operators inherit from the BaseOperator, which includes all of the required arguments for running work in Airflow.

Airflow completes work based on the arguments you pass to your operators.

## Task
To use an operator in a DAG, you have to instantiate it as a task. Tasks determine how to execute your operator’s work within the context of a DAG.

[//]: # (Notice how we pass a mix of operator specific arguments &#40;bash_command&#41; and an argument common to all operators &#40;retries&#41; inherited from BaseOperator to the operator’s constructor. This is simpler than passing every argument for every constructor call. Also, notice that in the second task we override the retries parameter with 3.)

