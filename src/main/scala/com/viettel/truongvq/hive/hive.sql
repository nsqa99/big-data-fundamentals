CREATE EXTERNAL TABLE presto.flight_data_temp(
   year INT,
   month INT,
   day INT,
   day_of_week INT,
   dep_time INT,
   crs_dep_time INT,
   arr_time INT,
   crs_arr_time INT,
   unique_carrier STRING,
   flight_num INT,
   tail_num STRING,
   actual_elapsed_time INT,
   crs_elapsed_time INT,
   air_time INT,
   arr_delay INT,
   dep_delay INT,
   origin STRING,
   dest STRING,
   distance INT,
   taxi_in INT,
   taxi_out INT,
   cancelled INT,
   cancellation_code STRING,
   diverted INT,
   carrier_delay STRING,
   weather_delay STRING,
   nas_delay STRING,
   security_delay STRING,
   late_aircraft_delay STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

CREATE EXTERNAL TABLE airports (
   name STRING,
   country STRING,
   area_code INT,
   code STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
LOCATION '/user/truongvq/flight_data';

CREATE EXTERNAL TABLE external_db.airports (
   name STRING,
   area_code INT,
   code STRING)
PARTITIONED BY (country STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

CREATE EXTERNAL TABLE external_db.flight_data(
   year INT,
    month INT,
   day INT,
   day_of_week INT,
   dep_time INT,
   crs_dep_time INT,
   arr_time INT,
   crs_arr_time INT,
   unique_carrier STRING,
   flight_num INT,
   tail_num STRING,
   actual_elapsed_time INT,
   crs_elapsed_time INT,
   air_time INT,
   arr_delay INT,
   dep_delay INT,
   distance INT,
   taxi_in INT,
   taxi_out INT,
   cancelled INT,
   cancellation_code STRING,
   diverted INT,
   carrier_delay STRING,
   weather_delay STRING,
   nas_delay STRING,
   security_delay STRING,
   late_aircraft_delay STRING
)
PARTITIONED BY (origin STRING, dest STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

CREATE EXTERNAL TABLE external_db.flight_data(
   year INT,
    month INT,
   day INT,
   day_of_week INT,
   dep_time INT,
   crs_dep_time INT,
   arr_time INT,
   crs_arr_time INT,
   unique_carrier STRING,
   flight_num INT,
   tail_num STRING,
   actual_elapsed_time INT,
   crs_elapsed_time INT,
   air_time INT,
   arr_delay INT,
   dep_delay INT,
   distance INT,
   taxi_in INT,
   taxi_out INT,
   cancelled INT,
   cancellation_code STRING,
   diverted INT,
   carrier_delay STRING,
   weather_delay STRING,
   nas_delay STRING,
   security_delay STRING,
   late_aircraft_delay STRING
)
PARTITIONED BY (origin STRING, dest STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

CREATE EXTERNAL TABLE external_db.flight_data_by_date(
   year INT,
   day_of_week INT,
   dep_time INT,
   crs_dep_time INT,
   arr_time INT,
   crs_arr_time INT,
   unique_carrier STRING,
   flight_num INT,
   tail_num STRING,
   actual_elapsed_time INT,
   crs_elapsed_time INT,
   air_time INT,
   arr_delay INT,
   dep_delay INT,
   origin STRING,
   dest STRING,
   distance INT,
   taxi_in INT,
   taxi_out INT,
   cancelled INT,
   cancellation_code STRING,
   diverted INT,
   carrier_delay STRING,
   weather_delay STRING,
   nas_delay STRING,
   security_delay STRING,
   late_aircraft_delay STRING
)
PARTITIONED BY (month int, day int)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ',';

CREATE EXTERNAL TABLE presto.airports_bucket (
   name STRING,
   country STRING,
   area_code INT,
   code STRING)
CLUSTERED BY (country) INTO 5 BUCKETS
STORED AS ORC;

CREATE EXTERNAL TABLE taxi_data (
passenger_count double,
trip_distance double,
RatecodeID double,
store_and_fwd_flag string,
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
airport_fee double
)
PARTITIONED BY (VendorID int)
STORED AS PARQUET
LOCATION '/user/truongvq/presto/taxi_data';

LOAD DATA INPATH '/user/truongvq/2008.csv' INTO TABLE external_db.flight_data_temp;
LOAD DATA INPATH '/user/truongvq/presto/taxi_data/yellow_tripdata_2022-01.parquet' INTO TABLE taxi_data;
LOAD DATA INPATH '/user/truongvq/airports.csv' INTO TABLE external_db.airports_temp;
INSERT OVERWRITE TABLE flight_data_with_partition PARTITION(month) SELECT * from  flight_data;
INSERT OVERWRITE TABLE airports PARTITION(country) SELECT name, area_code, code, country from  airports_temp;
INSERT OVERWRITE TABLE airports PARTITION(country) SELECT name, area_code, code, country from  airports_temp;
INSERT OVERWRITE TABLE flight_data_by_date PARTITION(month, day)
    SELECT year, day_of_week, dep_time, crs_dep_time, arr_time, crs_arr_time, unique_carrier, flight_num, tail_num, actual_elapsed_time, crs_elapsed_time,
        air_time, arr_delay, dep_delay, origin, dest, distance, taxi_in, taxi_out, cancelled, cancellation_code, diverted, carrier_delay, weather_delay,
        nas_delay, security_delay, late_aircraft_delay, month, day from flight_data_temp;

/home/truongvq/scala/big-data-fundamentals/src/main/scala/com/viettel/truongvq/hive/UpperCaseFistLetterUDF.java
INSERT OVERWRITE TABLE airports_bucket SELECT * FROM airports;
LOAD DATA INPATH '/user/truongvq/presto/airports/airports.csv' INTO TABLE presto.airports_bucket;

ALTER TABLE airports_with_skewed SKEWED BY (country) ON ('US') STORED AS DIRECTORIES;

CREATE FUNCTION lower_first_letter as 'com.viettel.truongvq.hive.LowerCaseFistLetterUDF';
add jar /home/truongvq/scala/big-data-fundamentals/target/hive.jar;

SELECT a1.name, count(a1.name) as total_flight, count(DISTINCT a2.name) as total_dest, sum(air_time) as total_time FROM flight_data_by_date f
JOIN airports a1 ON a1.code = f.origin
JOIN airports a2 ON a2.code = f.dest
WHERE distance > 1000 and a2.country = 'US'
GROUP BY a1.name

CREATE EXTERNAL TABLE data_test (
   id INT,
   uuid STRING,
   random_number INT
)
STORED AS ORC


