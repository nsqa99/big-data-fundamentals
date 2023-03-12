package com.viettel.anhnsq.spark_demo

import SparkSessionUtils.{closeSession, getSession}

object Joins {
  def main(args: Array[String]): Unit = {
    val spark = getSession
    import spark.implicits._

    val person = Seq(
      (0, "Bill Chambers", 0, Seq(100)),
      (1, "Matei Zaharia", 1, Seq(500, 250, 100)),
      (2, "Michael Armbrust", 1, Seq(250, 100))).toDF("id", "name", "graduate_program", "spark_status")
    val graduateProgram = Seq(
      (0, "Masters", "School of Information", "UC Berkeley"),
      (2, "Masters", "EECS", "UC Berkeley"),
      (1, "Ph.D.", "EECS", "UC Berkeley")).toDF("id", "degree", "department", "school")

    val sparkStatus = Seq(
      (500, "Vice President"),
      (250, "PMC Member"),
      (100, "Contributor")).toDF("id", "status")

    val joinExpression = person.col("graduate_program") === graduateProgram.col("id")
    person.join(graduateProgram, joinExpression).show()

    // inner join
    val joinType = "inner"
//    val joinType = "left_outer"
//    val joinType = "right_outer"
//    val joinType = "cross"
    person.join(graduateProgram, joinExpression, joinType).show()

    person.write.format("parquet")
      .option("mode", "overwrite")
      .parquet("./data/example/person.parquet")

    closeSession(spark)
  }
}
