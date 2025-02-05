CREATE DATABASE temperature_database;

USE temperature_database;

CREATE TABLE temperature_readings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    temperature DOUBLE NOT NULL,
    reading_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 select*from temperature_readings;
 SHOW tables;
 
