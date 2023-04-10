# Data warehouse
A data warehouse is a type of data management system that is designed to enable and support business intelligence (BI) activities, especially analytics. Data warehouses are solely intended to perform queries and analysis and often contain large amounts of historical data. The data within a data warehouse is usually derived from a wide range of sources such as application log files and transaction applications.

A data warehouse centralizes and consolidates large amounts of data from multiple sources. Its analytical capabilities allow organizations to derive valuable business insights from their data to improve decision-making. Over time, it builds a historical record that can be invaluable to data scientists and business analysts. Because of these capabilities, a data warehouse can be considered an organization’s “single source of truth.”

A typical data warehouse often includes the following elements:

A relational database to store and manage data
An extraction, loading, and transformation (ELT) solution for preparing the data for analysis
Statistical analysis, reporting, and data mining capabilities
Client analysis tools for visualizing and presenting data to business users
Other, more sophisticated analytical applications that generate actionable information by applying data science and artificial intelligence (AI) algorithms, or graph and spatial features that enable more kinds of analysis of data at scale

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

