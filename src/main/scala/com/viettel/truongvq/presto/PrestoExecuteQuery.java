package com.viettel.truongvq.presto;

import java.sql.*;

public class PrestoExecuteQuery {
    public static void main(String[] args) {
//        String sql = "select count(*) from flight_data_temp f";
//        String sql = "SELECT count(*) from airports a join flight_data_by_date f on f.origin = a.code group by code";
        String sql = "SELECT a1.name, count(a1.name) as total_flight, count(DISTINCT a2.name) as total_dest, sum(air_time) as total_time FROM flight_data_by_date f\n" +
                "JOIN airports a1 ON a1.code = f.origin\n" +
                "JOIN airports a2 ON a2.code = f.dest\n" +
                "WHERE distance > 1000 and a2.country = 'US'\n" +
                "GROUP BY a1.name";
        String url = "jdbc:presto://localhost:8080/hive/external_db";
        try (Connection connection = DriverManager.getConnection(url, "scott", null);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                System.out.print(rsmd.getColumnName(i + 1) + "   |   ");
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    System.out.print(rs.getString(i + 1) + "   |   ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
