# Apache NiFi

### What is Apache NiFi?

* Problem
* Advantages

### Terms

#### FlowFile

#### Processor

#### Relationship

#### Connection

#### Processor Group

#### Controller Service

#### Parameter Context

### Architecture
![architecture](images/nifi-architecture.png)
* Web Server
* Flow Controller
* FlowFile Repository
* Content Repository
* Provenance Repository

### Case studies
* Pull files (text, csv...) through FTP, transform and write to HDFS/local disk.
* Extract data from RDBMS, transform and write to HDFS/local disk.
* Pull data(JSON, files...) from RestAPI through HTTP, transform and write to HDFS/local disk.