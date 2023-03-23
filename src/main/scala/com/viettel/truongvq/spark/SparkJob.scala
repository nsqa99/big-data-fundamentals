package com.viettel.truongvq.spark

import org.apache.spark.sql.SparkSession

object SparkJob  {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("SparkExample")
      .config("spark.driver.extraClassPath", "src/main/resources/postgresql-42.6.0.jar")
      //    .config("spark.jars.packages", "org.postgresql:postgresql:42.6.0")
      .getOrCreate()


    val carFine = spark.read.option("multiline", "true")
//      .json("/airflow-data/data.json")
      .json(spark.conf.get("spark.executorEnv.location"))
//      .json("src/main/resources/data.json").toDF()
    val carFineModify = carFine.drop("id").withColumnRenamed("kindOfVehicle", "vehicle")
      .withColumnRenamed("teamAddress", "team_address").withColumnRenamed("licensePlate", "license_plate")

    carFineModify
      .write.format("jdbc")
      .mode("append")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://localhost:5432/postgres")
      .option("dbtable", "car_fine")
      .option("user", "airflow")
      .option("password", "airflow")
      .save()
  }
}
