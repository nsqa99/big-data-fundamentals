package com.viettel.truongvq.spark

import org.apache.spark.sql.SparkSession

object Spark extends App{
  val spark = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExample")
    .getOrCreate();

  val myCollection = "Spark The Definitive Guide : Big Data Processing Made Simple"
    .split(" ")
  val words = spark.sparkContext.parallelize(myCollection, 2)
//  val keyword = words.keyBy(word => word.toLowerCase.toSeq(0).toString)
//  print(keyword.lookup("s"))
  val chars = words.flatMap(word => word.toLowerCase.toSeq)
  val KVcharacters = chars.map(letter => (letter, 1))
//  print(KVcharacters.countByKey())
//  print(KVcharacters.groupByKey().map(row => (row._1, row._2.sum)).collect().mkString("Array(", ", ", ")"))
//  print(KVcharacters.reduceByKey((x,y) => x + y).collect().mkString("Array(", ", ", ")"))
//  println(words.countByValue())
//  println(words.map(word => (word, word(0))).collect().mkString("Array(", ", ", ")"))
//  println(words.mapPartitions(iterator => iterator.map(word => (word, word(0)))).collect().mkString("Array(", ", ", ")"))
//  words.foreachPartition { iter =>
//    while (iter.hasNext) {
//      println(iter.next())
//    }
//  }
//  val word2 = words.glom().collect()
  val nums = spark.sparkContext.parallelize(1 to 30, 5)
//  print(nums.aggregate(0)((x, y) => x.max(y), (x,y) => x + y))
//  print(KVcharacters.aggregateByKey(0)((x, y) => x + y, (x, y) => x.max(y)).collect().mkString("Array(", ", ", ")"))
  print(KVcharacters.foldByKey(0)((x, y) => x + y).collect().mkString("Array(", ", ", ")"))
  println()

}
