package com.viettel.anhnsq.spark_demo

import SparkSessionUtils.{closeSession, getSession}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.functions.{asc, avg, col, collect_list, collect_set, count, countDistinct, desc, expr, first, last, max, min, rank, sum, to_date, to_timestamp}

object Aggregations {
  def main(args: Array[String]): Unit = {
    val spark = getSession
    val fileReader: FileReader = new FileReader(spark, CSV,
      Map("inferSchema"-> "true", "header" -> "true"))
    val optionFileData: Option[DataFrame] = fileReader.load("./data/retail-data/all/online-retail-dataset.csv")

    optionFileData match {
      case Some(df) =>
        // count
        df.select(count("StockCode")).show()

        // countDistinct
        df.select(countDistinct("StockCode")).show()

        // first & last
        df.select(first("StockCode"), last("StockCode")).show()

        // min & max
        df.select(min("Quantity"), max("Quantity")).show()

        // sum
        df.select(sum("Quantity")).show()

        // avg
        df.select(
          count("Quantity").alias("total_transactions"),
          sum("Quantity").alias("total_purchases"),
          avg("Quantity").alias("avg_purchases"))
          .selectExpr(
            "total_purchases/total_transactions",
            "avg_purchases").show()

        // aggregating to complex type
        df.agg(collect_set("Country"), collect_list("Country")).show()

        // Group by
        df.groupBy("InvoiceNo").agg(
          count("Quantity").alias("quan"),
          expr("count(Quantity)")
        ).show()

        // Group by with maps
        df.groupBy("InvoiceNo").agg(
          "Quantity"->"avg",
          "Quantity"->"stddev_pop"
        ).show()

        // Window functions
        val dfWithDate = df.withColumn("date", to_date(col("InvoiceDate"), "M/d/y H:m"))
        val windowSpec = Window
          .partitionBy("CustomerId", "date")
          .orderBy(col("Quantity").desc)

        val maxPurchaseQuantity = max(col("Quantity")).over(windowSpec)
        val purchaseRank = rank().over(windowSpec)

        dfWithDate.where("CustomerId IS NOT NULL").orderBy("CustomerId")
          .select(
            col("CustomerId"),
            col("Quantity"),
            col("date"),
            col("Description"),
            purchaseRank.alias("quantityRank"),
            maxPurchaseQuantity.alias("maxPurchaseQuantity")
          ).show()

        // Grouping sets

      case None =>
    }

    closeSession(spark)
  }
}
