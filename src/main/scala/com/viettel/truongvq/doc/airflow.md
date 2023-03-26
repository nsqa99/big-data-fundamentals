# Apache Airflow
Apache Airflow is an open-source platform for developing, scheduling, and monitoring batch-oriented workflows.  A web interface helps manage the state of your workflows.

- Dynamic: DAGs are written in Python, allowing for dynamic pipeline creation.
- Extensible: Easily create your own operators, executors, and libraries.
- Elegant: Airflow DAGs are lean and explicit

# Architecture
![alt](arch-diag-basic.png)
- Scheduler: The scheduler is at the core of Airflow and manages anything and everything related to DAG runs, tasks, the task runs, parsing, and storing DAGs
- Web Server: This is the UI of Airflow. A user interface where users can view, control and monitor all DAGs. This interface provides functionality to trigger dag or a task manually, clear DAG runs, view task states & logs and view tasks run-duration
- Executor: An executor is a part of scheduler that handles and manages the running tasks
- Worker: A place where the tasks run
- Metadata DB: A database that stores workflow states, run duration, logs locations etc. This database also stores information regarding users, roles, connections, variables
- Dag Directory: A place where we store DAG
# Fundamental Concept

## DAGs (Directed Acyclic Graph)
A collection of all the tasks you want to run, organized in a way that reflects their relationships and dependencies.
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
### DAG Runs
A DAG Run is an object representing an instantiation of the DAG in time. Any time the DAG is executed, a DAG Run is created and all tasks inside it are executed. The status of the DAG Run depends on the tasks states. Each DAG Run is run separately from one another, meaning that you can have many runs of a DAG at the same time

DAGs will run in one of two ways:
- When they are triggered either manually or via the API
- On a defined schedule, which is defined as part of the DAG

### Scheduler

## Task
Task defines a unit of work within a DAG. It is represented as a node in the DAG

### Task Instances
An instance of a Task is a specific run of that task for a given DAG (and thus for a given data interval). They are also the representation of a Task that has state, representing what stage of the lifecycle it is in

### Task Lifecycle
![alt](task_lifecycle_diagram.png)
## Operators
An operator describes a single task in a workflow

Using operators helps to visualize task dependencies in our DAG code

All operators inherit from the BaseOperator, which includes all of the required arguments for running work in Airflow

Airflow has a very extensive set of operators available, with some built-in to the core or pre-installed providers

### Custom Operator

### Sensor
Sensors are a special type of Operator that are designed to do exactly one thing - wait for something to occur. It can be time-based, or waiting for a file, or an external event, but all they do is wait until something happens, and then succeed so their downstream tasks can run

## Control flow
By default, a DAG will only run a Task when all the Tasks it depends on are successful. There are several ways of modifying this, however:

- Branching, where you can select which Task to move onto based on a condition

- Latest Only, a special form of branching that only runs on DAGs running against the present

- Depends On Past, where tasks can depend on themselves from a previous run

- Trigger Rules, which let you set the conditions under which a DAG will run a task.

### Branching
### Latest Only
### Depends On Past
### Trigger Rules
- all_success (default): All upstream tasks have succeeded
- all_failed: All upstream tasks are in a failed or upstream_failed state
- all_done: All upstream tasks are done with their execution
- all_skipped: All upstream tasks are in a skipped state
- one_failed: At least one upstream task has failed (does not wait for all upstream tasks to be done)
- one_success: At least one upstream task has succeeded (does not wait for all upstream tasks to be done)
- one_done: At least one upstream task succeeded or failed
- none_failed: All upstream tasks have not failed or upstream_failed - that is, all upstream tasks have succeeded or been skipped
- none_failed_min_one_success: All upstream tasks have not failed or upstream_failed, and at least one upstream task has succeeded
- none_skipped: No upstream task is in a skipped state - that is, all upstream tasks are in a success, failed, or upstream_failed state
- always: No dependencies at all, run this task at any time

## XComs
XComs (short for “cross-communications”) are a mechanism that let Tasks talk to each other

## Hook, Connection

## XComs


