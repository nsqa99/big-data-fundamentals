package com.viettel.anhnsq.spark_demo

import SparkSessionUtils._
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.functions.{asc, col, desc, expr}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object SparkStructuredAPIBasicQueries {
  def main(args: Array[String]): Unit = {
    val spark = getSession
    val schema = StructType(Array(
      StructField("dest_country_name", StringType),
      StructField("origin_country_name", StringType),
      StructField("count", IntegerType)
    ))
    val fileReader: FileReader = new FileReader(spark, CSV,
      Map("inferSchema"-> "true", "header" -> "true"), schema)
    val optionFileData: Option[DataFrame] = fileReader.load("./data/flight-data/csv/2010-summary.csv")

    optionFileData match {
      case Some(df) =>
        // SELECT using .select
        df.select(
          col("dest_country_name"),
          col("count").as("quantity")
        ).show(2)

        // SELECT using .selectExpr
        df.selectExpr("dest_country_name as dest", "count as quantity").show(2)

        // Add column
        df.withColumn(
          "more_than_one_hundred_flights",
          expr("count > 100")
        ).show(2)

        // Rename column
        df.withColumnRenamed(
          "count", "quantity"
        ).show(2)

        // Filtering rows
        df.where("count > 100").show(2)
        // === df.filter(col("count") > 100).show(2)

        // Sorts
        df.sort("count").show(5)
        df.orderBy(desc("count")).show(5)
        df.orderBy(asc("count"), desc("dest_country_name")).show(5)

        //Union
        val newRows = Seq(
          Row("Viet Nam", "Japan", 5),
          Row("Thailand", "Laos", 1)
        )

        val parallelizedRows = spark.sparkContext.parallelize(newRows)
        val newDF = spark.createDataFrame(parallelizedRows, schema)

        df.union(newDF)
          .where("count = 1")
          .where(col("origin_country_name") =!= "United States")
          .show()

      case None =>
    }

    closeSession(spark)
  }
}
