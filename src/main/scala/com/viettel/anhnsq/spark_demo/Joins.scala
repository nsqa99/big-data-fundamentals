package com.viettel.anhnsq.spark_demo

import SparkSessionUtils.{closeSession, getSession}

object Joins {
  def main(args: Array[String]): Unit = {
    val spark = getSession
    import spark.implicits._

    val person = Seq(
      (0, "AnhNSQ", 0, Seq(101)),
      (1, "Harry Potter", 1, Seq(101, 202)),
      (2, "Jon Snow", 1, Seq(202, 303)))
      .toDF("id", "name", "graduate_program", "professor")
    val graduateProgram = Seq(
      (0, "Masters", "C2", "MIT"),
      (2, "Masters", "C1", "MIT"),
      (1, "Ph.D.", "A2", "Princeton")).toDF("id", "degree", "department", "school")

    val sparkStatus = Seq(
      (101, "James Bond"),
      (202, "Harry Maguire"),
      (303, "Chi Xen")).toDF("id", "name")

    val joinExpression = person.col("graduate_program") === graduateProgram.col("id")
    person.join(graduateProgram, joinExpression).show()

    // inner join
    val joinType = "inner"
//    val joinType = "left_outer"
//    val joinType = "right_outer"
//    val joinType = "cross"
    person.join(graduateProgram, joinExpression, joinType).show()

//    person.write.format("parquet")
//      .parquet("./data/example/person.parquet")

    closeSession(spark)
  }
}
