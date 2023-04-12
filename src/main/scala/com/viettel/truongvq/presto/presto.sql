CREATE SCHEMA presto_example;
CREATE TABLE taxi_data (
 tpep_pickup_datetime timestamp,
 tpep_dropoff_datetime timestamp,
 passenger_count double,
 trip_distance double,
 RatecodeID double,
 store_and_fwd_flag varchar,
 PULocationID int,
 DOLocationID int,
 payment_type int,
 fare_amount double,
 extra double,
 mta_tax double,
 tip_amount double,
 tolls_amount double,
 improvement_surcharge double,
 total_amount double,
 congestion_surcharge double,
 airport_fee double,
 VendorID int
) with (
    format = 'Parquet',
     partitioned_by = ARRAY['VendorID'],
     bucketed_by = ARRAY['PULocationID', 'DOLocationID'],
     bucket_count = 10,
    external_location = 'hdfs:/user/truongvq/taxi_data/yellow_tripdata_2022-01_copy_1.parquet'
)