package com.viettel.anhnsq.spark_demo

import com.viettel.anhnsq.spark_demo.SparkSessionUtils.{closeSession, getSession}
import org.apache.spark.sql.functions.{col, udf}

/**
 * @author anhnsq@viettel.com.vn
 */
object UserDefinedFunctions {
  def power3(number:Double): Double = number * number * number

  def main(args: Array[String]): Unit = {
    val spark = getSession

    val udfExampleDF = spark.range(5).toDF("num")
    val power3udf = udf(power3(_:Double):Double)

    udfExampleDF.select(power3udf(col("num"))).show()
    spark.udf.register("power3", power3(_:Double): Double)

    udfExampleDF.selectExpr("power3(num) as 3_powered").show()

    closeSession(spark)
  }
}
