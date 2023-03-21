package com.viettel.anhnsq.spark_demo

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

sealed trait FileFormat
case object CSV extends FileFormat
case object JSON extends FileFormat
case object Parquet extends FileFormat
case object Text extends FileFormat

class FileReader(spark: SparkSession, format: FileFormat, options: Map[String, String], schema: StructType = new StructType()) {
  def load(filePath: String): Option[DataFrame] = {
    try {
      val dfReader = spark.read.format(format.toString)
      dfReader.options(options)
      if (schema.nonEmpty) {
        dfReader.schema(schema)
      }

      Some(dfReader.load(filePath))
    } catch {
      case e: Exception =>
        println(s"Exception occurs, message = ${e.getMessage}")
        None
    }
  }
}