package com.viettel.truongvq.presto;

import java.sql.*;

public class PrestoExample {
    public static void main(String[] args) {
        String sql = "select count(*) from flight_data_temp f cross join airports group by f.origin";
        String url = "jdbc:presto://localhost:8080/hive/external_db";
        try (Connection connection = DriverManager.getConnection(url, "test", null);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getLong(1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
