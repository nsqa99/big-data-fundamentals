# Apache Airflow
Apache Airflow is an open-source platform for developing, scheduling, and monitoring batch-oriented workflows.

The main characteristic of Airflow workflows is that all workflows are defined in Python code. “Workflows as code” serves several purposes:

- Dynamic: Airflow pipelines are configured as Python code, allowing for dynamic pipeline generation.

- Extensible: The Airflow framework contains operators to connect with numerous technologies. All Airflow components are extensible to easily adjust to your environment.

- Flexible: Workflow parameterization is built-in leveraging the Jinja templating engine.

# Architecture
![alt](arch-diag-basic.png)
- Web Server
- Scheduler
- Executor
- Worker
- Metadata DB
# Fundamental Concept

## DAGs (Directed Acyclic Graph)
A collection of all the tasks you want to run, organized in a way that reflects their relationships and dependencies.
The Python script purpose is to define DAG object.

DAGs will run in one of two ways:
- When they are triggered either manually or via the API
- On a defined schedule, which is defined as part of the DAG

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

### Scheduler

## Task
Task defines a unit of work within a DAG. It is represented as a node in the DAG.

### Task Instances
### Task Lifecycle

## Operators
Using operators helps to visualize task dependencies in our DAG code.

All operators inherit from the BaseOperator, which includes all of the required arguments for running work in Airflow.

Airflow completes work based on the arguments you pass to your operators.

### Bash Operator
### ...
### Custom Operator
### Sensor
### DAG Assignment
### Bitshift Composition
### Relation Builder

## (Optional) Hook, Connection, Pool, Queues

## XComs


