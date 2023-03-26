# Apache NiFi

```
Generated Username f961d68f-222f-48df-bb6d-0cca5efd646b
Generated Password I+tvuqhiiUcw4RpOpADmyI3sV+Eajvwu
```

## What is Apache NiFi?

* A dataflow system based on the concepts of flow-based programming.
* NiFi was built to automate the flow of data between systems.
* Term `dataflow` in NiFi's context: the automated and managed flow of information between systems.
* Solves the problems occur when enterprises had more than one system: Some created data and some consumes data.
* High-level challenges of dataflow:
    * Systems fail.
    * Data access exceeds capacity to consume.
    * Boundary conditions are mere suggestions: data is too big, too small, too fast, too slow, corrupt, wrong, or in
      the wrong format.
    * Enabling new flows and changing existing ones must be fast.
    * Vary system properties: Dataflow exists to connect what is essentially a massively distributed system of
      components that are loosely or not-at-all designed to work together.
    * Compliance and security: System to system and system to user interactions must be secure, trusted, accountable.
    * Continuous improvement occurs in production

## Core concepts

| Term               | Description                                                                                                                                                                                                                                                                                              |
|--------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| FlowFile           | Represents each object moving through the system and for each one, NiFi keeps track of a map of key/value pair attribute strings and its associated content of zero or more bytes                                                                                                                        |
| FlowFile Processor | Processors do some combination of data routing, transformation, or mediation between systems.<br/>Processors have access to attributes of a given FlowFile and its content stream.<br/>Processors can operate on zero or more FlowFiles in a given unit of work and either commit that work or rollback. |
| Connection         | Provides the actual linkage between processors.<br/>Act as queues, allow various processes to interact at different rates.                                                                                                                                                                               |
| Flow Controller    | Similar to scheduler: Maintains the knowledge of how processes connect and manages the threads and allocations the processes use                                                                                                                                                                         |
| Process Group      | A specific set of processes and their connections, which can receive data via input ports and send data out via output ports.                                                                                                                                                                            |

### FlowFile

* Composed of attributes and contents.
* Content: the data that is represented by the FlowFile.
* Attributes: characteristics that provide information or context about the data (metadata).
    * Standard attributes:
        * uuid
        * filename
        * path
* Provenance: a record of what has happened to the FlowFile

### Processor

* Listen for incoming data.
* Pull data from external sources and publish data to external sources.
* Route, transform, or extract information from FlowFiles.
* Upstream connections: Connections that are coming into the Processor.
* Downstream connections: Connections that are going out of the Processor.

### Relationship

After a Processor has finished processing a FlowFile, it will route (or “transfer”) the FlowFile to one of the
Relationships.
A DataFlow Manager (DFM) then able to connect each of these Relationships to other components in order to specify where
the FlowFile should go next.

### Connection

* Components are connected together via Connections.
* Each connection consists of one or more Relationships.
* When a FlowFile is transferred to a particular Relationship, it is added to the queue belonging to the associated
  Connection.

#### Back Pressure

* Object Threshold: Number of FlowFiles can be in queue before back pressure is applied.
* Data Size Threshold: Maximum size of data in queue before back pressure is applied.

#### Load balancing

Strategies to distribute data in a flow across the nodes in the cluster

* Do not balance: Not balancing FlowFiles between nodes. (default)
* Partition by attribute:
    * Determine which node to send a FlowFile to based on the value of user-defined attribute.
    * If the destination node is unavailable, the data will not fail over to another node and will wait for the node to
      be available again.
* Round-robin:
    * FlowFiles will be distributed to nodes in the cluster in a round-robin way.
    * If the destination node is unavailable, the data will be automatically redistributed to another node(s).
* Single node:
    * All FlowFiles will be sent to a single node in the cluster.
    * Which node they are sent to is not configurable.
    * If the destination node is unavailable, the data will remain in queue until the node is available again.

### Process Group

* NiFi allows multiple components to be grouped into a Process Group.
* Connect to other components via Input and Output Ports.

### Template

* A dataflow comprised of many sub-flows that could be reused.
* A part of dataflow (or entire dataflow) can be extracted as a Template for reusing.

### Controller Service

Shared services that can be used by reporting tasks, processors, and other services to utilize for configuration or task
execution.

### Parameters

#### Parameter Context

* Parameters are created within Parameter Contexts.
* Parameter Contexts are globally defined/accessible to the NiFi instance.

#### Parameter Provider

* Parameter Providers allow parameters to be stored in sources external to NiFi.
* The parameters of a Parameter Provider can be fetched and applied to all referencing Parameter Contexts.

## Architecture

NiFi executes within a JVM on a host operating system

* **Web Server:** host NiFi's HTTP-based command and control APIs.
* **Flow Controller:** Provides threads for extensions to run on, manages the schedule of when extensions receive
  resources to execute.
* **Extensions:** Operate and execute within the JVM
* **FlowFile repository:**
    * Store FlowFile attributes.
    * Write-Ahead Log: each change to the FlowFiles is logged in the FlowFile Repository before it happens as a
      transactional unit of work.
    * NiFi recovers a FlowFile by restoring a "snapshot" of the FlowFile (created when the Repository is check-pointed),
      then replaying each of the changes in the log file.
      A snapshot is taken periodically by the system, default: 2 mins interval.
    * Two main locations that the FlowFile exists:
        * Write-Ahead Log (FlowFile Repository).
        * A hash map in working memory:
            * Has a reference to all FlowFiles actively being used in the Flow.
            * Processors reference to FlowFiles objects held in connection queues in memory, referenced by this hash
              map.
    * Default implementation: persistent Write-Ahead Log located on a specified disk partition.
    * When a change occurs to the FlowFile, the change is written out to the Write-Ahead Log and the object in memory is
      modified accordingly.
    * "Swapping" FlowFiles:
        * Occurs when the FlowFile in a connection queue exceeds the threshold.
        * FlowFiles with the lowest priority in the queue will be written to a swap file, then they will be removed by
          the hash map. The connection queue is in charge of determining when to swap those FlowFiles back into the
          memory.
* **Content repository:**
    * Store FlowFile's content on disk and only read it into JVM when needed.
    * Made up of a collection of files, which are binned into Containers and Sections, on disk.
        * A Section is a subdirectory of a Container.
        * A Container can be thought of as a root directory for the Content Repository.
        * The Content Repository is made up of many Containers => NiFi can take advantage of multiple physical
          partitions in parallel.
    * A dedicated thread in NiFi analyzes the Content repo for un-used contents, then remove or archive them.
    * Default implementation: stores block of data in the file system.
    * More than one file system storage location can be specified.
* **Provenance repository:**
    * Store the history of each FlowFile => provide data lineage.
    * Each time the FlowFile is created, forked, cloned, modified, etc., a new provenance event is created.
    * When a provenance event is created, it copies all the FlowFile’s attributes and the pointer to the FlowFile’s
      content and aggregates that with the FlowFile’s state to one location in the Provenance Repo.
    * Since Provenance is not copying the content in the Content Repo, and just copying the FlowFile’s pointer to the
      content, the content could be deleted before the provenance event that references it is deleted
      => Cannot see the content or replay the FlowFile.
    * Default implementation: Use one or more physical disk volumes. Event data is indexed and searchable.
      NiFi is available to operate within a cluster.

## Case studies

* Fetch files (text, csv...) from FTP, transform and write to HDFS/local disk.
* Extract data from RDBMS, transform and write to HDFS/local disk.
* Pull data(JSON, files...) from RestAPI through HTTP, transform and write to HDFS/local disk.