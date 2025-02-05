import time
import serial
import Adafruit_DHT

# Configure Serial Communication
ser = serial.Serial('/dev/ttyUSB0', 9600, timeout=1)  # Change to COM3 if using Windows

# DHT Sensor Config
DHT_SENSOR = Adafruit_DHT.DHT11  # Change to DHT22 if needed
DHT_PIN = 2  # GPIO pin connected to DHT sensor

while True:
    temperature = Adafruit_DHT.read(DHT_SENSOR, DHT_PIN)

    if temperature is not None:
        print(f"Sending Temperature: {temperature}°C")
        ser.write(f"{temperature}\n".encode())  # Send data over Serial
    else:
        print("Failed to read temperature!")

    time.sleep(5)  # Wait before sending the next reading
