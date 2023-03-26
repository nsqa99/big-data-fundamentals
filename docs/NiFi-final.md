# Apache NiFi

## What is Apache NiFi?

* NiFi was built to automate the flow of data between systems.
* Dataflow in NiFi context: The automated and managed flow of information between systems.

### Problem

* High level challenges of dataflow:
  * Systems fail: Networks fail, disks fail, software crashes, people make mistakes.
  * Data access exceeds capacity to consume.
  * Boundary conditions are mere suggestions: data is too big, too small, too fast, too slow, corrupt, wrong, or in
    the wrong format.
  * Enabling new flows and changing existing ones must be fast.
  * Vary system properties: Dataflow exists to connect what is essentially a massively distributed system of
    components that are loosely or not-at-all designed to work together.
  * Compliance and security: System to system and system to user interactions must be secure, trusted, accountable.
  * Continuous improvement occurs in production: It's often not possible to replicate production environments in the
    lab.

### Key features

* Flow management
  * Guaranteed delivery: achieved through persistent write-ahead log and content repository.
  * Data buffering: supports buffering of all queued data, provides back pressure when queues reach limit.
  * Prioritized Queuing: allows defining how data is retrieved from a queue in a prioritized way.
  * Flow Specific QoS (high latency or high throughput): provides a way to configure for choosing between high latency
    or high throughput.
* Ease of use
  * Provides visual command and control: allows creating and modifying flows using UI only.
  * Supports compliance, debug, optimization with data provenance.
* Security
* Extensible architecture
  * NiFi core built for extensions: dataflow processes can execute and interact in a predictable and repeatable
    manner.
  * Allow extensions of multiple components: Processors, Controller Services, Reporting Tasks, Prioritizers, and
    Customer User Interfaces.
* Flexible scaling model
  * Scale-out: NiFi is designed to scale-out through the use of clustering many nodes together.
  * Scale-up & down: Easy to increase/decrease throughput of NiFi by increasing/decreasing the number of concurrent
    tasks on the processor by modifying processor settings on UI.

## Core components

### FlowFile

* Represents each object moving through the system.
* Made up of two components: FlowFile Attributes and FlowFile Content.
  * Content: the data that is represented by the FlowFile.
  * Attributes: characteristics that provide information or context about the data (metadata).
    * Standard attributes:
      * uuid
      * filename
      * path

### Processor

* Responsible for creating, sending, receiving, transforming, routing, splitting, merging, and processing FlowFiles.

### Relationship

* Each Processor has zero or more Relationships.
* Processed FlowFiles will be routed to one of the Processor's Relationships.

### Connection

* NiFi components are connected together via Connections.
* Each connection consists of one or more Relationships.
* When a FlowFile is transferred to a particular Relationship, it is added to the queue belonging to the associated
  Connection.

### Processor Group

* A group of Processors.
* When dataflow becomes complex, Processors should be grouped to present dataflow at a higher, more abstract level.

### Controller Service

* Extensions that added to NiFi.
* Shared services that can be used by reporting tasks, processors, and other services to utilize for configuration or
  task
  execution.

### Parameter Context

* Parameters are created within Parameter Contexts.
* Parameter Contexts are globally defined/accessible to the NiFi instance.
* A Process Group can only be assigned one Parameter Context, while a given Parameter Context can be assigned to
  multiple Process Groups.

## Architecture

![architecture](images/nifi-architecture.png)

* Web Server: Host NiFi’s HTTP-based command and control API.
* Flow Controller
  * The brains of the operation
  * Provides threads for extensions to run on, manages the schedule of when extensions receive
    resources to execute.
* Extensions:
  * Extensions of NiFi.
  * Operate and execute within the JVM.
* FlowFile Repository
  * Is a "Write-Ahead Log" (or data record) of the metadata of each of the FlowFiles that currently exist in the system.
  * FlowFile metadata includes:
    * All the attributes associated with the FlowFile.
    * A pointer to the actual content of the FlowFile.
    * The state of the FlowFile, such as which Connection/Queue the FlowFile belongs in.
  * As FlowFiles flow through the system, each change to the FlowFiles is logged in the FlowFile Repository before it
    happens as a transactional unit of work.
  * NiFi recovers a FlowFile by restoring a "snapshot" of the FlowFile (created when the Repository is check-pointed),
    then replaying each of the changes in the log file.
    A snapshot is taken periodically (default: 2 minutes) by the system.
* Content Repository
  * A place in local storage where the content of all FlowFiles exists.
  * Utilizes the immutability and copy-on-write paradigms to maximize speed and thread-safety.
  * The core design decision is to hold the FlowFile’s content on disk and only read it into JVM memory when it’s needed
    => Processing large objects will not harm memory.
  * Made up of a collection of files on disk, which are binned into Containers and Sections.
    * A Section is a subdirectory of a Container.
    * A Container can be thought of as a root directory for the Content Repository.
    * The Content Repository is made up of many Containers
      => NiFi can take advantage of multiple physical partitions in parallel.
  * A dedicated thread in NiFi analyzes the Content repo for un-used contents, then remove or archive them.
* Provenance Repository
  * Stores the history of each FlowFile => provides data lineage.
  * Each time the FlowFile is created, forked, cloned, modified, etc., a new provenance event is created.
  * When a provenance event is created, it copies all the FlowFile’s attributes and the pointer to the FlowFile’s
    content and aggregates that with the FlowFile’s state to one location in the Provenance Repo.
  * Since Provenance is not copying the content in the Content Repo, and just copying the FlowFile’s pointer to the
    content, the content could be deleted before the provenance event that references it is deleted.
  * Event data is indexed and searchable.

* FlowFiles in memory and on disk
  * FlowFiles that are actively being processed by the system are held in a hash map in the JVM memory.
  * The in-memory hash map has a reference to all FlowFiles actively being used in the flow:
    Objects used by Processors and held in connection queues are the same as those referenced by this hash map.
  * When a change occurs to the FlowFile, the change is written out to the Write-Ahead Log and the object in memory is
    modified accordingly.
  * "Swapping" FlowFiles:
    * Occurs when the FlowFile in a connection queue exceeds the threshold.
    * FlowFiles with the lowest priority in the queue will be written to a swap file, then they will be removed by
      the hash map. The connection queue is in charge of determining when to swap those FlowFiles back into the
      memory.

## Case studies

* Pull files (text, csv...) through FTP, transform and write to HDFS/local disk.
* Extract data from RDBMS, transform and write to HDFS/local disk.
* Pull data(JSON, files...) from RestAPI through HTTP, transform and write to HDFS/local disk.
* Pull data from multiple sources, transform, merge, then write to HDFS/local disk.

## FAQ

### NiFi tasks distribution differences between Clustering and Single Instance NiFi

### Compare NiFi and AirFlow