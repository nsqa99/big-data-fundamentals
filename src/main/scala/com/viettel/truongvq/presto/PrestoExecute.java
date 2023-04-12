package com.viettel.truongvq.presto;

import java.sql.*;

public class PrestoExecute {
    public static void main(String[] args) {
//        String sql = "select count(*) from flight_data_temp f";
        String sql =
                "CREATE TABLE taxi_data (\n" +
                " tpep_pickup_datetime timestamp,\n" +
                " tpep_dropoff_datetime timestamp,\n" +
                " passenger_count double,\n" +
                " trip_distance double,\n" +
                " RatecodeID double,\n" +
                " store_and_fwd_flag varchar,\n" +
                " PULocationID int,\n" +
                " DOLocationID int,\n" +
                " payment_type int,\n" +
                " fare_amount double,\n" +
                " extra double,\n" +
                " mta_tax double,\n" +
                " tip_amount double,\n" +
                " tolls_amount double,\n" +
                " improvement_surcharge double,\n" +
                " total_amount double,\n" +
                " congestion_surcharge double,\n" +
                " airport_fee double,\n" +
                " VendorID int\n" +
                ") with (\n" +
                "    format = 'PARQUET',\n" +
                "     partitioned_by = ARRAY['VendorID'],\n" +
                "     bucketed_by = ARRAY['PULocationID', 'DOLocationID'],\n" +
                "     bucket_count = 10,\n" +
                "    external_location = 'hdfs://localhost:9000/user/truongvq/presto/taxi_data'\n" +
                ")";
        String url = "jdbc:presto://localhost:8080/hive/presto_example";
        try (Connection connection = DriverManager.getConnection(url, "scott", null);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            System.out.println(ps.execute());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
