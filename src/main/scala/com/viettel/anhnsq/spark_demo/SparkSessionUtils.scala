package com.viettel.anhnsq.spark_demo

import org.apache.spark.sql.SparkSession

object SparkSessionUtils {
  def getSession: SparkSession = {
    SparkSession.builder
      .master("local[1]")
      .appName("demoApp")
      .getOrCreate()
  }

  def closeSession(session: SparkSession): Unit = {
    session.close()
  }
}
