package com.viettel.truongvq.hive

import java.sql.DriverManager


object HiveExamples extends App {
  val conStr = "jdbc:hive2://localhost:10000/external_db";
//  var sql = "SELECT count(*) FROM flight_data_temp f JOIN airports a JOIN airports p"
//  var sql = "SELECT `arr.*` FROM flight_data_temp LIMIT 1"
//  var sql = "SELECT * FROM airports WHERE name RLIKE '.*(City|County).*'"
  var sql = "SELECT a1.name, count(1), sum(air_time) FROM flight_data_temp f\n" + "JOIN airports_temp a1 ON a1.code = f.origin\n" + "JOIN airports_temp a2 ON a2.code = f.dest\n" + "WHERE distance > 1000 and a2.country = 'US'\n" + "GROUP BY a1.name";
  var con = DriverManager.getConnection(conStr);
  try {
    val stmt = con.createStatement()
    val rs = stmt.executeQuery(sql)
    val rsmd = rs.getMetaData
    for (i <- Range(0, rsmd.getColumnCount)) {
      print(rsmd.getColumnName(i + 1) + "   |   ")
    }
    println()
    while (rs.next()) {
      for (i <- Range(0, rsmd.getColumnCount)) {
        print(rs.getString(i + 1) + "   |   ")
      }
      println()
    }

  } catch {
    case ex: Exception => println(ex.printStackTrace())
  } finally {
    con.close()
  }
}
