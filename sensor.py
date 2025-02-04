import time
import mysql.connector
import Adafruit_DHT
from datetime import datetime

# MySQL database connection
conn = mysql.connector.connect(
    host="localhost",
    user="your_username",  # Replace with your MySQL username
    password="your_password",  # Replace with your MySQL password
    database="sensor_data"  # Replace with your database name
)
cursor = conn.cursor()

# Create table if not exists
cursor.execute('''
    CREATE TABLE IF NOT EXISTS temperature (
        id INT AUTO_INCREMENT PRIMARY KEY,
        timestamp DATETIME,
        temperature FLOAT
    )
''')
conn.commit()

# DHT sensor configuration
DHT_SENSOR = Adafruit_DHT.DHT11  # Change to DHT22 if needed
DHT_PIN = 2  # GPIO pin connected to DHT sensor

def store_temperature(temperature):
    # Get the current timestamp
    timestamp = datetime.now()

    # Insert the data into the MySQL table
    cursor.execute("INSERT INTO temperature (timestamp, temperature) VALUES (%s, %s)",
                   (timestamp, temperature))
    conn.commit()
    print(f"Stored data: {timestamp} - {temperature} °C")

def read_and_store_data():
    while True:
        # Read temperature and humidity from DHT sensor
        humidity, temperature = Adafruit_DHT.read(DHT_SENSOR, DHT_PIN)

        # Check if the temperature reading is valid
        if temperature is not None:
            print(f"Temperature: {temperature} °C")
            store_temperature(temperature)  # Store data into MySQL
        else:
            print("Failed to read temperature from DHT sensor!")

        # Wait before reading again
        time.sleep(5)  # Adjust the sleep time as needed

if __name__ == "__main__":
    read_and_store_data()
