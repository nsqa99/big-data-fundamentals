# Apache Spark
Spark is a unified analytics engine for large-scale data processing

## Cluster manager
Cluster manager will manage the clusters of machines that Spark use to execute tasks on data.
  * Spark standalone's cluster manager.
  * YARN.
  * Mesos.

## Spark Applications
* Two processes:
  * Driver:
    * Maintaining information about the Spark Application.
    * Responding to a user's program or input.
    * Analyzing, distributing and scheduling work across the executors.
  * Executor: responsible for carrying out the work that the driver assigns them.

* Spark applications need being submitted to the cluster manager, so that cluster manager will provide resources to the application to run.

## Spark APIs
Two fundamental sets of APIs:
  * Low-level "unstructed" APIs.
  * Higher-level structured APIs.

## Spark core
  * Datasets
  * DataFrames
  * Resilient Distributed Datasets (RDDs)

**SparkSession**
* Is a driver process.
* Executes user-defined manipulations across the cluster.

### DataFrames
* The most common Structured API.
* Represents a table of data with rows and columns.
* Spark DataFrame can be partitioned and span thousands of computers, since the data is either too large to fit on one machine or it takes too long to perform the computation on one machine.
* A DataFrame consists of a series of records (like rows in a table), that are of type Row, and a number of columns (like columns in a spreadsheet) that represent a computation expression that can be performed on each individual record in the Dataset.

### Dataset
* A distributed collection of data.
* DataFrame is Dataset of type Row.
* Using Dataset will slow down operation due to the conversion from type Row to the case class type.
* When to use Dataset:
  * When the operation(s) you would like to perform cannot be expressed using DataFrame manipulations
  * When you want or need type-safety, and you’re willing to accept the cost ofperformance to achieve it

### Transformations
* Core data structures in Spark are **immutable**.
* To change a DataFrame, need **transformations** to instruct Spark how to modify them.
* Transfromations are the core of how to express business logic using Spark.
* Two types of transformations:
  * Narrow dependencies:
    * Each input partition will contribute to only one output partition.
    * Spark will automatically perform an operation called **pipelining**, meaning that if we specify multiple filters on DataFrames, they'll all be performed **in-memory**.

  * Wide dependencies:
    * Input partitions contributing to many output partitions.
    * Often referred to as a shuffle whereby Spark will exchange partitions across the cluster.
    * Results will be written to disk.

### Lazy evaluation
Spark will not evaluate transformations until it meets an action.

## Structured APIs

### Structured Spark types
* Spark uses an internal engine called Catalyst to maintain its own type information through the planning and processing of work.
=> This avoid using JVM types, which can cause high garbage-collection and object instantiation cost.

**Overview of structured APIs execution**

The process of executing code on clusters:
1. Write DataFrame/Dataset/SQL code.
2. If code is valid, Spark converts it to a Logical Plan.
3. Spark transform this Logical Plan to a Physical Plan, checking for optimizations along the way.
4. Spark then executes this Physical Plan (RDD manipulations) on the cluster.

### Basic structured operations
* Schemas define the name as well as the type of data in each column of Dataset (also DataFrame)
```
val df = spark.read.format("json")
  .load("/example/data.json")

df.printSchema()
```
* We can either let a data source define the schema (called schema-on-read) or we can define it explicitly ourselves.
Schema-on-read may lead to wrong type inference.

* Manual define schema:
```
import org.apache.spark.sql.types.{StructField, StructType, StringType, LongType}
import org.apache.spark.sql.types.Metadata

val myManualSchema = StructType(Array(
  StructField("DEST_COUNTRY_NAME", StringType, true),
  StructField("ORIGIN_COUNTRY_NAME", StringType, true),
  StructField("count", LongType, false)
))

val df = spark.read.format("json")
  .schema(myManualSchema)
  .load("/example/data.json")
```

* Reference to columns:
```
df.col("col_name")
```

* Create DataFrames on the fly by taking a set of rows and converting them to a DataFrame
```
// in Scala
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StructField, StructType, StringType, LongType}

val myManualSchema = new StructType(Array(
  new StructField("some", StringType, true),
  new StructField("col", StringType, true),
  new StructField("names", LongType, false))
)

val myRows = Seq(Row("Hello", null, 1L))
val myRDD = spark.sparkContext.parallelize(myRows)
val myDf = spark.createDataFrame(myRDD, myManualSchema)

myDf.show()
```
* Query data
  
  `select` and `selectExpr` allow you to do the DataFrame equivalent of SQL queries on a table of data
  ```
  // in Scala
  df.select("DEST_COUNTRY_NAME").show(2)

  df.selectExpr(
    "*", // include all original columns
    "DEST_COUNTRY_NAME as destination_country_name")
  .show(2)

  -- in SQL
  SELECT DEST_COUNTRY_NAME FROM dfTable LIMIT 2
  ```

* Add column
```
df.withColumn("withinCountry", expr("ORIGIN_COUNTRY_NAME == DEST_COUNTRY_NAME")).show(2)
```

* Rename column
```
df.withColumnRenamed("DEST_COUNTRY_NAME", "dest").columns
```

* Removing columns
```
df.drop("col1", "col2").columns
```

* Filtering rows
```
df.filter(col("count") < 2).show(2)
df.where("count < 2").show(2)

-- in SQL
SELECT * FROM dfTable WHERE count < 2 LIMIT 2
```

* Union
```
// in Scala
import org.apache.spark.sql.Row

val schema = df.schema
val newRows = Seq(
  Row("New Country", "Other Country", 5L),
  Row("New Country 2", "Other Country 3", 1L)
)

val parallelizedRows = spark.sparkContext.parallelize(newRows)
val newDF = spark.createDataFrame(parallelizedRows, schema)

df.union(newDF)
  .where("count = 1")
  .where($"ORIGIN_COUNTRY_NAME" =!= "United States")
  .show() // get all of them and we'll see our new rows at the end
```

* Sorting rows
```
// in Scala
df.sort("counter").show(5)
df.orderBy("counter", "DEST_COUNTRY_NAME").show(5)
df.orderBy(asc("counter"), desc("DEST_COUNTRY_NAME")).show(5)
df.orderBy(col("counter"), col("DEST_COUNTRY_NAME")).show(5)
```

* Collect rows to the driver
Any collection of data to the driver can be a very expensive operation: driver can be crashed if the dataset is too large.

### Working with data types
* Null:
  * Remove rows contain nulls:
    ```
    df.na.drop()
    df.na.drop("any")
    ```

### Aggregations

### Joins

### Datasource
**Read data**
```
spark.read.format("csv")
  .option("mode", "FAILFAST")
  .option("inferSchema", "true")
  .option("path", "path/to/file(s)")
  .schema(someSchema)
  .load()
```
