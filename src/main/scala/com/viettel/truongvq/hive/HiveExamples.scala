package com.viettel.truongvq.hive

import java.sql.DriverManager


object HiveExamples extends App {
  val conStr = "jdbc:hive2://localhost:10000/external_db";
  Class.forName("org.apache.hive.jdbc.HiveDriver");
  var con = DriverManager.getConnection(conStr);
  try {
    val stmt = con.createStatement();
    val rs = stmt.executeQuery("select count(*) from flight_data_temp f join airports a join airports p");
    val rsmd = rs.getMetaData
    for (i <- Range(0, rsmd.getColumnCount)) {
      print(rsmd.getColumnName(i + 1) + "   |   ")
    }
    println()
    while (rs.next()) {
      for (i <- Range(0, rsmd.getColumnCount)) {
        print(rs.getString(i + 1) + "   |   ")
      }
    }

  } catch {
    case ex: Exception => println(ex.printStackTrace())
  } finally {
    con.close()
  }
}
