# Hadoop notes

**Filesystem:** is a method and data structure that the operating system uses to control how data is stored and retrieved.

**Distributed system terms:**
  * Node: a physical separated computer in the distributed system network.
  * Cluster: a collection of nodes.
  * Instance: a virtual machine of a running program.
  * Architecture:
    * Master-slave: Slaves communicate to master only, and receive commands from master.
    * Peer-to-peer: Every node in the network communicate with each other.

## HDFS Concepts

### Racks
* A collection of 30-40 DataNodes or machines in a Hadoop cluster located in a single data center or location.
* A cluster contains multiple racks.

### Blocks
* A disk has a block size: the minimum amount of data that it can read or write.
* Filesystems for a single disk build on this by dealing with data in blocks, which are an integral multiple of the disk block size.
Filesystem blocks are typically a few kilobytes in size, whereas disk blocks are normally 512 bytes.

* HDFS block size: 128MB default.
* Unlike a filesystem for a single disk, a file in HDFS that is smaller than a single block does not occupy a full block's worth of underlying storage: a 1MB file stored with a block size of 128MB uses 1MB of disk space, not 128MB.

**Benefits of having block abstraction**
* A file can be larger than any single disk in the network => blocks from the file can spread through cluster's available disks.
* Making the unit of abstraction a block rather than a file simplifies the storage subsystem: block size is fixed => easier for calculation.
* Blocks fit well with replication for providing fault tolerance and availability.

### Namenode and datanodes
* Two types of nodes operating in master-worker pattern of an HDFS cluster:
  * Namenode: master
  * Datanodes: workers

**Namenodes**
* Namenode manages the filesystem namespace: 
  
  * Maintains the filesystem tree and the metadata for all the files and directories in the tree.
  This information is stored persistently on the local disk in the form of two files: the namespace image (fsImage) and the edit log (EditLog). 
  
  * Namespace means a container: in this context, it means the file name grouping or hierarchy structure.
  
  * Keeps a reference to every file and blocks in the filesystem **in memory**

**Datanodes**
* Store and retrieve blocks in command of  clients or the namenode.
* Report back to the namenode periodically with lists of blocks which they are storing.

### Data flow

**Read data**
* Client calls Namenode to determine using RPC to determine the locations of the blocks of the file need reading.
* For each block, the Namenode returns the addresses of the datanodes that have a copy of that block, then client will connect to those datanodes to read data.

**Write data**
* Client calls Namenode to create a new file, using RPC.
* Namenode will check if the create command is valid, then create a file in the filesystem's namespace.
* Client will receive a stream to write data to, data will be split into packets and send to a data queue.
* Data in the queue will then be write to every datanodes
