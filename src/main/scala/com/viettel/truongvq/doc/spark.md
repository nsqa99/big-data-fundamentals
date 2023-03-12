# APACHE SPARK
Apache Spark is an Open source analytical processing engine for large scale powerful distributed data processing and machine learning applications.

## Spark architecture
![alt](spark-architechture.png)

### Driver process
The driver process is absolutely essential—it’s the heart of a Spark Application and maintains all relevant information during the lifetime of the application
- Maintaining information about the Spark Application
- Responding to a user’s program or input 
- Analyzing, distributing, and scheduling work across the executors

### Executors 
The executors are responsible for actually carrying out the work that the driver assigns them.
- Executing code assigned to it by the driver
- Reporting the state of the computation on that executor back to the driver node

### Cluster manager
A driver program controls the execution of jobs and stores data in a cache. At the outset, executors register with the drivers. This executor has a number of time slots to run the application concurrently. Executors read and write external data in addition to servicing client requests. A job is executed when the executor has loaded data and they have been removed in the idle state. The executor is dynamically allocated, and it is constantly added and deleted depending on the duration of its use. A driver program monitors executors as they perform users’ tasks. Code is executed in the Java process when an executor executes a user’s task.

Spark employs a cluster manager that keeps track of the resources available.
The driver process is responsible for executing the driver program’s commands across the
executors to complete a given task.

### SparkSession

You control your Spark Application through a driver process called the SparkSession. The SparkSession instance is the way Spark executes user-defined manipulations across the cluster

### DataFrames
Represents a table of data with rows and columns

### Datasets
The Dataset API gives users the ability to assign a Java/Scala class to the records within a DataFrame and manipulate it as a collection of typed objects

### Structured Streaming

### Machine Learning and Advanced Analytics

### Partitions
Spark breaks up the data into chunks called partitions

### Transformations
- Narrow dependencies
- Wide dependencies

### Actions

## Spark Low-level APIs

### SparkContext

### RDD(Resilient Distributed Datasets)
Represents an immutable, partitioned collection of records which computes on the different node of the cluster
- Resilient: The fault-tolerant as they can track data lineage information to allow for rebuilding lost data automatically on failure. To achieve fault tolerance for the generated RDD’s, the achieved data is replicated among various Spark executors in worker nodes in the cluster
- Distributed: Data present in RDD resides on multiple nodes. It is distributed across different nodes of a cluster
- Datasets: Represents records of the data
### Creating RDDs
One of the easiest ways to get RDDs is from an existing DataFrame or Dataset
```
// Converts a Dataset[Long] to RDD[Long]
spark.range(500).rdd

spark.range(10).toDF().rdd.map(rowObject => rowObject.getLong(0))
```
Create an RDD from a collection
```
val myCollection = "Spark The Definitive Guide : Big Data Processing Made Simple"
.split(" ")
val words = spark.sparkContext.parallelize(myCollection, 2)
```
Or from data source
```
// This creates an RDD for which each record in the RDD represents a line in that text file or files
spark.sparkContext.textFile("/some/path/withTextFiles")

// This creates an RDD for which each text file should become a single record
spark.sparkContext.wholeTextFiles("/some/path/withTextFiles")
```

### Transformations
- distinct: A distinct method call on an RDD removes duplicates from the RDD
```
words.distinct().count()
```
- filter: Find in our record which ones match some predicate function
```
def startsWithS(individual:String) = {
    individual.startsWith("S")
}

words.filter(word => startsWithS(word)).collect()
```
- map: Returns the value that you want from each record
```
// map the current word to the word and its starting letter. Ex: “Spark,” “S,”
val words2 = words.map(word => (word, word(0)))
```
- flatMap: Flattens the records
```
words.flatMap(word => word.toSeq).take(5)
```
- sort: Sort an RDD
```
words.sortBy(word => word.length() * -1).take(2)
```
- mapPartitions: Execute map once per each partitions
```
words.mapPartitions(iterator => iterator.map(word => (word, word(0))))
```
- foreachPartition: Iterates over all the partitions of the data
```
  words.foreachPartition { iter =>
    while (iter.hasNext) {
      println(iter.next())
    }
  }
```
- glom: Takes data in each partition of your dataset and converts them to arrays
```
words.glom().collect()
// Array(Array("Spark", "The", "Definitive", "Guide", ":"), Array("Big", "Data", "Processing", "Made", "Simple"))
```
### Actions
- reduce: Reduce an RDD of any kind of value to one value
```
spark.sparkContext.parallelize(1 to 20).reduce(_ + _) // 210

def wordLengthReducer(leftWord:String, rightWord:String): String = {
    if (leftWord.length > rightWord.length)
        return leftWord
    else
        return rightWord
}
words.reduce(wordLengthReducer)
```
- count: Count the number of records in the RDD
```
words.count()
```
- countByValue: Counts the number of values in a given RDD
```
words.countByValue()
```
- max and min: Return the maximum values and minimum values
```
spark.sparkContext.parallelize(1 to 20).max() // 20
spark.sparkContext.parallelize(1 to 20).min() // 1
```
- take: Take a number of values from your RDD. It works by first scanning one partition, and use the results from that partition to estimate the number of additional partitions needed to satisfy the limit.
```
words.take(5) // Take the first 5 elements of the RDD.
words.takeOrdered(5) // Get the 5 elements from an RDD ordered in ascending order or as specified by the optional key function.
words.top(5) // Get the top 5 elements from an RDD.
val withReplacement = true
val numberToTake = 6
val randomSeed = 100L
words.takeSample(withReplacement, numberToTake, randomSeed) // Return a fixed-size sampled subset of this RDD.
```

[//]: # (### Saving Files)

[//]: # (- saveAsTextFile: Save to a text file)

[//]: # (```)

[//]: # (words.saveAsTextFile&#40;"file:/tmp/bookTitle"&#41;)

[//]: # (```)

[//]: # (- SequenceFiles: A sequenceFile is a flat file consisting of binary key–value pairs. It is extensively used in MapReduce as input/output formats)

[//]: # (```)

[//]: # (words.saveAsObjectFile&#40;"/tmp/my/sequenceFilePath"&#41;)

[//]: # (```)
[//]: # (### Pipe RDDs to System Commands)
### Key-Value RDDs
keyBy: Create a key for a records and keeps the record as the value for the keyed RDD
```
val keyword = words.keyBy(word => word.toLowerCase.toSeq(0).toString)
```
mapValues: Map over the values 
```
keyword.mapValues(word => word.toUpperCase).collect()
```
Extracting Keys and Values: 
```
keyword.keys.collect()
keyword.values.collect()
```
lookup: Look up the result for a particular key
```
keyword.lookup("s") // WrappedArray(Spark, Simple)
```
countByKey: Count the number of elements for each key
```
val chars = words.flatMap(word => word.toLowerCase.toSeq)
val KVcharacters = chars.map(letter => (letter, 1))
KVcharacters.countByKey()
```
groupByKey: Group the values for each key in the RDD into a single sequence
```
KVcharacters.groupByKey().map(row => (row._1, row._2.sum)).collect()
```
reduceByKey: Merge the values for each key using an associative and commutative reduce function.
```
KVcharacters.reduceByKey((x,y) => x + y).collect()
```
aggregate: The first aggregates within partitions, the second aggregates across partitions. The start value will be used at both aggregation levels
```
val nums = sc.parallelize(1 to 30, 5)
nums.aggregate(0)((x, y) => x.max(y), (x,y) => x + y) // Sum max of each partition
```
aggregateByKey: Same as aggregate but instead of doing it partition by partition, it does it by key
```
KVcharacters.aggregateByKey(0)((x,y) => x + y), (x, y) => x.max(y)).collect()
```
### Joins
### Controlling Partitions
### Custom Serialization