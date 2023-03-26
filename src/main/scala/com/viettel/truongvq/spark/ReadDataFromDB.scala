package com.viettel.truongvq.spark

import org.apache.spark.sql.SparkSession

object ReadDataFromDB {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("SparkExample")
      .config("spark.driver.extraClassPath", "src/main/resources/postgresql-42.6.0.jar")
      //    .config("spark.jars.packages", "org.postgresql:postgresql:42.6.0")
      .getOrCreate()

    val df = spark.read.format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://localhost:5432/postgres")
      .option("user", "airflow")
      .option("password", "airflow")
      .option("query", "select car_fine.*, car.customer, car.phone_number from car_fine\njoin car on car_fine.license_plate = car.license_plate")
      .load()

    df.show()

  }
}
