package com.viettel.truongvq.spark

import org.apache.spark.sql.SparkSession

object ReadDataAndInsertDB {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("SparkExample")
      .config("spark.driver.extraClassPath", "src/main/resources/postgresql-42.6.0.jar")
      //    .config("spark.jars.packages", "org.postgresql:postgresql:42.6.0")
      .getOrCreate()


    val carFine = spark.read.option("multiline", "true")
//            .json("/car-fine-data-short.json")
      .json(spark.conf.get("spark.executorEnv.location"))

    val carFineDF = carFine.drop("id").withColumnRenamed("kindOfVehicle", "vehicle")
      .withColumnRenamed("teamAddress", "team_address").withColumnRenamed("licensePlate", "license_plate")

    carFineDF
      .write.format("jdbc")
      .mode("append")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://localhost:5432/postgres")
      .option("dbtable", "car_fine")
      .option("user", "airflow")
      .option("password", "airflow")
      .save()

    val carDF = spark.read.format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://localhost:5432/postgres")
      .option("user", "airflow")
      .option("password", "airflow")
      .option("query", "select car.customer, car.phone_number, car.license_plate from car")
      .load()

    val info = carDF.join(carFineDF, carDF("license_plate") === carFineDF("license_plate")).groupBy("phone_number")
  }
}
