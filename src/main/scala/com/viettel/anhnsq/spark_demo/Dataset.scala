package com.viettel.anhnsq.spark_demo

import com.viettel.anhnsq.spark_demo.SparkSessionUtils.{closeSession, getSession}

object Dataset {
  case class Flight(DEST_COUNTRY_NAME: String, ORIGIN_COUNTRY_NAME: String, count: BigInt)
  case class FlightMetadata(count: BigInt, randomData: BigInt)

  def main(args: Array[String]): Unit = {
    val spark = getSession
    import spark.implicits._

    val flightsDF = spark.read.parquet("./data/flight-data/parquet/2010-summary.parquet/")
    val flights = flightsDF.as[Flight]

    // Filter
    def originIsDestination(flight_row: Flight): Boolean = {
      flight_row.ORIGIN_COUNTRY_NAME == flight_row.DEST_COUNTRY_NAME
    }

    flights.filter(flight_row => originIsDestination(flight_row)).first()

    // Map
    val destinations = flights.map(f => f.DEST_COUNTRY_NAME)
    println(destinations.take(2).mkString("[", ", ", "]"))

    // join
    val flightsMeta = spark.range(500).map(x => (x, scala.util.Random.nextLong))
      .withColumnRenamed("_1", "count").withColumnRenamed("_2", "randomData")
      .as[FlightMetadata]

    val flights2 = flights.join(flightsMeta, Seq("count"))
    println(flights2.take(5).mkString("[", ", ", "]"))

    // joinWith
    val flightsUsingJoinWith = flights.joinWith(
      flightsMeta,
      flights.col("count") === flightsMeta.col("count"))
    println(flightsUsingJoinWith.take(5).mkString("[", ", ", "]"))

    // Group by
    def grpSum(countryName:String, values: Iterator[Flight]) = {
      values.dropWhile(_.count < 5).map(x => (countryName, x))
    }

    flights.groupByKey(x => x.DEST_COUNTRY_NAME).flatMapGroups(grpSum).show(5)

    closeSession(spark)
  }
}
