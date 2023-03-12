package com.viettel.truongvq.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{HashPartitioner, Partitioner}


object CustomPartitioning extends App {
  class DomainPartitioner extends Partitioner {
    def numPartitions = 3

    def getPartition(key: Any): Int = {
      val customerId = key.asInstanceOf[Double].toInt
      if (customerId == 17850.0 || customerId == 12583.0) {
        return 0
      } else {
        return new java.util.Random().nextInt(2) + 1
      }
    }
  }

  val spark = SparkSession.builder()
    .master("local[1]")
    .appName("CustomPartitioning")
    .getOrCreate();

  val df = spark.read.option("header", "true").option("inferSchema", "true")
    .csv("src/main/resources/online-retail-dataset.csv")
  val rdd = df.coalesce(10).rdd
  rdd.map(r => r(6)).take(5).foreach(println)
  val keyedRDD = rdd.keyBy(row => row(6).asInstanceOf[Int].toDouble)
  keyedRDD
    .partitionBy(new DomainPartitioner).map(_._1).glom().map(_.toSet.toSeq.length)
    .take(5)

}
