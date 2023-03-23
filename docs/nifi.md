# Apache NiFi

```
Generated Username f961d68f-222f-48df-bb6d-0cca5efd646b
Generated Password I+tvuqhiiUcw4RpOpADmyI3sV+Eajvwu
```

## What is Apache NiFi?

* A dataflow system based on the concepts of flow-based programming.
* NiFi was built to automate the flow of data between systems.
* Term `dataflow` in NiFi's context: the automated and managed flow of information between systems.
* Problem it solves:
* Solve the problems occurred when enterprises had more than one system: Some created data and some consumes data.
* High-level challenges of dataflow:
    * Systems fail.
    * Data access exceeds capacity to consume.
    * Boundary conditions are mere suggestions: data is too big, too small, too fast, too slow, corrupt, wrong, or in
      the wrong format.
    * Enabling new flows and changing existing ones must be fast.
    * Vary system properties: Dataflow exists to connect what is essentially a massively distributed system of
      components
      that are loosely or not-at-all designed to work together.
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

* Content: the data that is represented by the FlowFile.
* Attributes: characteristics that provide information or context about the data.
  Standard attributes:
    * uuid
    * filename
    * path

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

### flow.xml.gz

* NiFi UI canvas is written to one file in realtime called `flow.xml.gz`.
* Stored in `nifi/conf` by default.
* NiFi automatically creates a backup copy of this file when it is updated.

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
* **FlowFile repository:** Where NiFi keeps track of the state of what it knows about a given FlowFile that is presently
  active in the flow.
    * Pluggable implementation.
    * Default implementation: persistent Write-Ahead Log located on a specified disk partition.
* **Content repository:** Where the actual content bytes of a given FlowFile live.
    * Pluggable implementation.
    * Default implementation: stores block of data in the file system.
        * More than one file system storage location can be specified.
* **Provenance repository:** Where all provenance event data is stored.
    * Pluggable implementation.
    * Default implementation: Use one or more physical disk volumes. Event data is indexed and searchable.

NiFi is available to operate within a cluster.

## Case studies
* Pull files (text, csv...) through FTP, transform and write to HDFS/local disk.
* Extract data from RDBMS, transform and write to HDFS/local disk.
* Pull data(JSON, files...) from RestAPI through HTTP, transform and write to HDFS/local disk.