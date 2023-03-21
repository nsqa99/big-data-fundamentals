# Big data

## What is big data?
* Data sets that are too large or complex to be dealt with by traditional data-processing application software within a tolerable elapsed time. (wikipedia)
* Three key characteristics of big data:
    * Volume
    * Velocity
    * Variety

## Applications
* Product development: Used for building predictive models to increase user experience
* Business: Used for analyzing to predict trends and making business plans
* Machine learning: Used for training machine learning models

## Hadoop
### Overview
* An opensource framework that allows for the distributed processing of large data sets across clusters of computers
  using simple programming models.

### Architecture
![hadoop-architecture-layer](./images/hadoop-ecosystem-layers.png)

### HDFS

A filesystem designed for storing very large files with streaming data access patterns, running on clusters of commodity hardware

#### Block
* Disk block size: the minimum amount of data that it can read or write.
* Filesystems for a single disk build on this by dealing with data in blocks, which are an integral multiple of the disk block size.
  Filesystem blocks are typically a few kilobytes in size, whereas disk blocks are normally 512 bytes.
* HDFS block size: 128MB default.

#### Architecture
![hdfs-architecture-layer](./images/hdfs-architecture.gif)
* Using master-slave architecture

* **Namenodes**
    * Namenode manages the filesystem namespace:

        * Maintains the filesystem tree and the metadata for all the files and directories in the tree.
          This information is stored persistently on the local disk in the form of two files: the namespace image (fsImage) and the edit log (EditLog).

        * Namespace means a container: in this context, it means the file name grouping or hierarchy structure.

        * Keeps a reference to every file and blocks in the filesystem **in memory**

* **Datanodes**
    * Store and retrieve blocks in command of  clients or the namenode.
    * Report back to the namenode periodically with lists of blocks which they are storing.