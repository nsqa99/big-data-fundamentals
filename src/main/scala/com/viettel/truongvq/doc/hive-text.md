# Data warehouse
A data warehouse, or enterprise data warehouse (EDW), is a system that aggregates data from different sources into a single, central, consistent data store to support data analysis, data mining, artificial intelligence (AI), and machine learning. A data warehouse system enables an organization to run powerful analytics on huge volumes (petabytes and petabytes) of historical data in ways that a standard database cannot.

A data warehouse centralizes and consolidates large amounts of data from multiple sources. Its analytical capabilities allow organizations to derive valuable business insights from their data to improve decision-making. Over time, it builds a historical record that can be invaluable to data scientists and business analysts. Because of these capabilities, a data warehouse can be considered an organization’s “single source of truth.”

A typical data warehouse often includes the following elements:

Bottom tier: The bottom tier consists of a data warehouse server, usually a relational database system, which collects, cleanses, and transforms data from multiple data sources through a process known as Extract, Transform, and Load (ETL) or a process known as Extract, Load, and Transform (ELT).
Middle tier: The middle tier consists of an OLAP (i.e. online analytical processing) server which enables fast query speeds. Three types of OLAP models can be used in this tier, which are known as ROLAP, MOLAP and HOLAP. The type of OLAP model used is dependent on the type of database system that exists.
Top tier: The top tier is represented by some kind of front-end user interface or reporting tool, which enables end users to conduct ad-hoc data analysis on their business data.

Four unique characteristics (described by computer scientist William Inmon, who is considered the father of the data warehouse) allow data warehouses to deliver this overarching benefit. According to this definition, data warehouses are

Subject-oriented. They can analyze data about a particular subject or functional area (such as sales).
Integrated. Data warehouses create consistency among different data types from disparate sources.
Nonvolatile. Once data is in a data warehouse, it’s stable and doesn’t change.
Time-variant. Data warehouse analysis looks at change over time.

# Hive flow


The data analyst executes a query with the User Interface (UI).
The driver interacts with the query compiler to retrieve the plan, which consists of the query execution process and metadata information. The driver also parses the query to check syntax and requirements.
The compiler creates the job plan (metadata) to be executed and communicates with the metastore to retrieve a metadata request.
The metastore sends the metadata information back to the compiler
The compiler relays the proposed query execution plan to the driver.
The driver sends the execution plans to the execution engine.
The execution engine (EE) processes the query by acting as a bridge between the Hive and Hadoop. The job process executes in MapReduce. The execution engine sends the job to the JobTracker, found in the Name node, and assigns it to the TaskTracker, in the Data node. While this is happening, the execution engine executes metadata operations with the metastore.
The results are retrieved from the data nodes.
The results are sent to the execution engine, which, in turn, sends the results back to the driver and the front end (UI).

The driver calls the user interface’s execute function to perform a query.
The driver answers the query, creates a session handle for the query, and passes it to the compiler for generating the execution plan.
The compiler responses to the metadata request are sent to the metaStore.
The compiler computes the metadata using the meta data sent by the metastore. The metadata that the compiler uses for type-checking and semantic analysis on the expressions in the query tree is what is written in the preceding bullet. The compiler generates the execution plan (Directed acyclic Graph) for Map Reduce jobs, which includes map operator trees (operators used by mappers and reducers) as well as reduce operator trees (operators used by reducers).
The compiler then transmits the generated execution plan to the driver.
After the compiler provides the execution plan to the driver, the driver passes the implemented plan to the execution engine for execution.
The execution engine then passes these stages of DAG to suitable components. The deserializer for each table or intermediate output uses the associated table or intermediate output deserializer to read the rows from HDFS files. These are then passed through the operator tree. The HDFS temporary file is then serialised using the serializer before being written to the HDFS file system. These HDFS files are then used to provide data to the subsequent MapReduce stages of the plan. After the final temporary file is moved to the table’s location, the final temporary file is moved to the table’s final location.
The driver stores the contents of the temporary files in HDFS as part of a fetch call from the driver to the Hive interface. The Hive interface sends the results to the driver.

# Driver 
Driver
Driver acts like a controller which receives the HiveQL statements. The driver starts the execution of statement by creating sessions. It monitors the life cycle and progress of the execution. Driver stores the necessary metadata generated during the execution of a HiveQL statement. It also acts as a collection point of data or query result obtained after the Reduce operation.

Compiler
Compiler performs the compilation of the HiveQL query. This converts the query to an execution plan. The plan contains the tasks. It also contains steps needed to be performed by the MapReduce to get the output as translated by the query. The compiler in Hive converts the query to an Abstract Syntax Tree (AST). First, check for compatibility and compile time errors, then converts the AST to a Directed Acyclic Graph (DAG).

Optimizer
It performs various transformations on the execution plan to provide optimized DAG. It aggregates the transformations together, such as converting a pipeline of joins to a single join, for better performance. The optimizer can also split the tasks, such as applying a transformation on data before a reduce operation, to provide better performance.

Executor
Once compilation and optimization complete, the executor executes the tasks. Executor takes care of pipelining the tasks.

CLI, UI, and Thrift Server
CLI (command-line interface) provide a user interface for an external user to interact with Hive. Thrift server in Hive allows external clients to interact with Hive over a network, similar to the JDBC or ODBC protocols.

ive Driver: The Hive driver receives the HiveQL statements submitted by the user through the command shell and creates session handles for the query.

Hive Compiler: Metastore and hive compiler both store metadata in order to support the semantic analysis and type checking performed on the different query blocks and query expressions by the hive compiler. The execution plan generated by the hive compiler is based on the parse results.

The DAG (Directed Acyclic Graph) is a DAG structure created by the compiler. Each step is a map/reduce job on HDFS, an operation on file metadata, and a data manipulation step.

Optimizer: The optimizer splits the execution plan before performing the transformation operations so that efficiency and scalability are improved.

Execution Engine: After the compilation and optimization steps, the execution engine uses Hadoop to execute the prepared execution plan, which is dependent on the compiler’s execution plan.

# Mapreduce 
Input data is split into smaller pieces, or "input splits", by the Hadoop Distributed File System (HDFS).

Each input split is assigned to a mapper node in the cluster, which processes the data in parallel with other mappers. The mapper reads the data from its assigned input split, performs a user-defined computation (specified by the MapReduce job), and outputs intermediate key-value pairs.

The intermediate key-value pairs produced by the mappers are shuffled and sorted by the framework, and then sent to the reducer nodes. This step is called the shuffle and sort phase.

Each reducer node processes the intermediate key-value pairs assigned to it, performing another user-defined computation (specified by the MapReduce job), and producing the final output key-value pairs.

The final output key-value pairs are collected and written to the output file by the HDFS.

# Presto fast
In the past, distributed query engines like Hive were designed to persist intermediate results to disk. As the below figure illustrates, Presto saves time by executing the queries in the memory of the worker machines, including performing operations on intermediate datasets there, instead of persisting them to disk. The data can reside in HDFS or any database or any data lake, and Presto performs the executions in-memory across your workers, shuffling data between workers as needed. Avoiding the need for writing and reading from disk between stages ultimately speeds up the query execution time. Hive intermediate data sets are persisted to disk. Presto executes tasks in-memory.

If this distributed in-memory model sounds familiar, that’s because Apache Spark uses the same basic concept to effectively replace MapReduce-based technologies. However, Spark and Presto manage stages differently. In Spark, data needs to be fully processed before passing to the next stage. Presto uses a pipeline processing approach and doesn’t need to wait for an entire stage to finish.
Presto was developed with the following design considerations:
• High performance with in-memory execution
• High scalability from 1 to 1000s of workers
• Flexibility to support a wide range of SQL use cases
• Highly pluggable architecture that makes it easy to extend Presto with custom integrations for security, event listeners, etc.
• Federation of data sources via Presto connectors
• Seamless integration with existing SQL systems by adhering to the ANSI SQL standard

# Hive and presto 
Presto is more focused on analytical querying whereas Hive is mostly used to facilitate data access. Hive provides a virtual data warehouse that imposes structure on semi-structured datasets, which can then be queried using Spark, MapReduce, or Presto itself

Presto has a limitation on the maximum amount of memory that each task in a query can store, so if a query requires a large amount of memory, the query simply fails.

# PResto flow
Now that you understand how any real-world deployment of Presto involves a cluster
with a coordinator and many workers, we can look at how an actual SQL query state‐
ment is processed.
48 | Chapter 4: Presto Architecture
Check out Chapters 8 and 9 to learn details about the SQL support
of Presto.
Understanding the execution model provides you the foundational knowledge neces‐
sary to tune Presto’s performance for your particular queries.
Recall that the coordinator accepts SQL statements from the end user, from the CLI
software using the ODBC or JDBC driver or other clients’ libraries. The coordinator
then triggers the workers to get all the data from the data source, creates the result
data set, and makes it available to the client.
Let’s take a closer look into what happens inside the coordinator first. When a SQL
statement is submitted to the coordinator, it is received in textual format. The coordi‐
nator takes that text and parses and analyzes it. It then creates a plan for execution by
using an internal data structure in Presto called the query plan. This flow is displayed
in Figure 4-6. The query plan broadly represents the needed steps to process the data
and return the results per the SQL statement.
Figure 4-6. Processing a SQL query statement to create a query plan
As you can see in Figure 4-7, the query plan generation uses the metadata SPI and the
data statistics SPI to create the query plan. So the coordinator uses the SPI to gather
information about tables and other metadata connecting to the data source directly.
Figure 4-7. The service provider interfaces for query planning and scheduling
Query Execution Model | 49
The coordinator uses the metadata SPI to get information about tables, columns, and
types. These are used to validate that the query is semantically valid, and to perform
type checking of expressions in the original query and security checks.
The statistics SPI is used to obtain information about row counts and table sizes to
perform cost-based query optimizations during planning.
The data location SPI is then facilitated in the creation of the distributed query plan.
It is used to generate logical splits of the table contents. Splits are the smallest unit of
work assignment and parallelism.
The different SPIs are more of a conceptual separation; the actual
lower-level Java API is separated by different Java packages in a
more fine-grained manner.
The distributed query plan is an extension of the simple query plan consisting of one
or more stages. The simple query plan is split into plan fragments. A stage is the run‐
time incarnation of a plan fragment, and it encompasses all the tasks of the work
described by the stage’s plan fragment.
The coordinator breaks up the plan to allow processing on clusters facilitating work‐
ers in parallel to speed up the overall query. Having more than one stage results in the
creation of a dependency tree of stages. The number of stages depends on the com‐
plexity of the query. For example, queried tables, returned columns, JOIN statements,
WHERE conditions, GROUP BY operations, and other SQL statements all impact the
number of stages created.
Figure 4-8 shows how the logical query plan is transformed into a distributed query
plan on the coordinator in the cluster.
Figure 4-8. Transformation of the query plan to a distributed query plan
50 | Chapter 4: Presto Architecture
The distributed query plan defines the stages and the way the query is to execute on a
Presto cluster. It’s used by the coordinator to further plan and schedule tasks across
the workers. A stage consists of one or more tasks. Typically, many tasks are involved,
and each task processes a piece of the data.
The coordinator assigns the tasks from a stage out to the workers in the cluster, as
displayed in Figure 4-9.
Figure 4-9. Task management performed by the coordinator
The unit of data that a task processes is called a split. A split is a descriptor for a seg‐
ment of the underlying data that can be retrieved and processed by a worker. It is the
unit of parallelism and work assignment. The specific operations on the data per‐
formed by the connector depend on the underlying data source.
For example, the Hive connector describes splits in the form of a path to a file with
offset and length that indicate which part of the file needs to be processed.
Tasks at the source stage produce data in the form of pages, which are a collection of
rows in columnar format. These pages flow to other intermediate downstream stages.
Pages are transferred between stages by exchange operators, which read the data from
tasks within an upstream stage.
The source tasks use the data source SPI to fetch data from the underlying data source
with the help of a connector. This data is presented to Presto and flows through the
engine in the form of pages. Operators process and produce pages according to their
semantics. For example, filters drop rows, projections produce pages with new
derived columns, and so on. The sequence of operators within a task is called a pipe‐
line. The last operator of a pipeline typically places its output pages in the task’s out‐
put buffer. Exchange operators in downstream tasks consume the pages from an
upstream task’s output buffer. All these operations occur in parallel on different
workers, as seen in Figure 4-10.
Query Execution Model | 51
Figure 4-10. Data in splits is transferred between tasks and processed on dierent
workers
So a task is the runtime incarnation of a plan fragment when assigned to a worker.
After a task is created, it instantiates a driver for each split. Each driver is an instantia‐
tion of a pipeline of operators and performs the processing of the data in the split. A
task may use one or more drivers, depending on the Presto configuration and envi‐
ronment, as shown in Figure 4-11. Once all drivers are finished, and the data is
passed to the next split, the drivers and the task are finished with their work and are
destroyed.
Figure 4-11. Parallel drivers in a task with input and output splits
An operator processes input data to produce output data for a downstream operator.
Example operators are table scans, filters, joins, and aggregations. A series of these
operators form an operator pipeline. For example, you may have a pipeline that first
scans and reads the data, and then filters on the data, and finally does a partial aggre‐
gation on the data.
To process a query, the coordinator creates the list of splits with the metadata from
the connector. Using the list of splits, the coordinator starts scheduling tasks on the
workers to gather the data in the splits. During query execution, the coordinator
tracks all splits available for processing and the locations where tasks are running on
workers and processing splits. As tasks finish processing and are producing more
splits for downstream processing, the coordinator continues to schedule tasks until
no splits remain for processing.
Once all splits are processed on the workers, all data is available, and the coordinator
can make the result available to the client.