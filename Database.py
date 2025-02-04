from flask import Flask, request, jsonify
import mysql.connector
from datetime import datetime

app = Flask(__name__)

# Database connection
conn = mysql.connector.connect(
    host="localhost",
    user="your_username",
    password="your_password",
    database="sensor_data"
)
cursor = conn.cursor()

# Create table if not exists
cursor.execute('''
    CREATE TABLE IF NOT EXISTS temperature (
        id INT AUTO_INCREMENT PRIMARY KEY,
        timestamp DATETIME,
        temperature FLOAT,
    )
''')
conn.commit()

@app.route('/store_data', methods=['POST'])
def store_data():
    data = request.get_json()
    temperature = data.get("temperature")
    timestamp = datetime.now()
    
    cursor.execute("INSERT INTO temperature (timestamp, temperature) VALUES (%s, %s)",
                   (timestamp, temperature))
    conn.commit()
    
    return jsonify({"message": "Data stored successfully"}), 200

if __name__ == '__main__':
    app.run(debug=True)
