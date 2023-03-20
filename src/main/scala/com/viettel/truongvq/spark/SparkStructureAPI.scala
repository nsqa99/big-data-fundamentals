package com.viettel.truongvq.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{LongType, Metadata, StringType, StructField, StructType}

object SparkStructureAPI extends App {
  val spark = SparkSession.builder()
    .master("local[1]")
    .appName("SparkExample")
    .getOrCreate();

  val customSchema = StructType(Array(StructField("DEST_COUNTRY_NAME", StringType, true),
    StructField("ORIGIN_COUNTRY_NAME", StringType, true),
    StructField("count", LongType, false,
      Metadata.fromJson("{\"name\":\"count\", \"type\": \"long\"}"))
  ))

  val df = spark.read.format("json").schema(customSchema)
    .load("src/main/resources/2015-summary.json")
  df.createOrReplaceTempView("tempView")
  print(spark.sql(
    """
  SELECT DEST_COUNTRY_NAME, sum(count)FROM tempView GROUP BY DEST_COUNTRY_NAME
  """)
    .where("DEST_COUNTRY_NAME like 'S%'").where("`sum(count)` > 10")
    .count())

  val sparkSchema = spark.read.format("json").load("src/main/resources/2015-summary.json")
//  print(sparkSchema.first())

}
