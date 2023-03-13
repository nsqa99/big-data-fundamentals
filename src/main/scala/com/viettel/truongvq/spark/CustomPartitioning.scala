package com.viettel.truongvq.spark

import com.viettel.truongvq.spark.Spark.spark
import org.apache.spark.api.java.JavaRDD.fromRDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{HashPartitioner, Partitioner}


object CustomPartitioning extends App {
  class DomainPartitioner extends Partitioner {
    def numPartitions = 3

    def getPartition(key: Any): Int = {
      new java.util.Random().nextInt(2)
    }
  }

  val spark = SparkSession.builder()
    .master("local[1]")
    .appName("CustomPartitioning")
    .getOrCreate();

  val myCollection = "Spark The Definitive Guide : Big Data Processing Made Simple"
    .split(" ")
  val words = spark.sparkContext.parallelize(myCollection, 4)
  val rdd = words.rdd
  val keyword = rdd.keyBy(word => word.toLowerCase.toSeq.head.toString)
  println(keyword.collect().mkString("Array(", ", ", ")"))
  println(keyword.glom().map(_.toSet.toSeq.length).collect().mkString("Array(", ", ", ")"))
  println(keyword.partitionBy(new DomainPartitioner).glom().map(_.toSet.toSeq.length).collect().mkString("Array(", ", ", ")"))

}
