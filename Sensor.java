import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

public class Sensor {
    // MySQL Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:<port_no>/<DB_name>";
    private static final String DB_USER = "your_username"; 
    private static final String DB_PASSWORD = "your_password"; 

    public static void main(String[] args) {
        // Open the Serial Port
        SerialPort comPort = SerialPort.getCommPort("COM3"); 
        comPort.setBaudRate(9600);
        
        if (!comPort.openPort()) {
            System.out.println("Failed to open serial port!");
            return;
        }

        System.out.println("Listening for sensor data...");

        Scanner scanner = new Scanner(comPort.getInputStream());
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            while (true) {
                // Read data from serial
                if (scanner.hasNextLine()) {
                    String data = scanner.nextLine().trim();
                    try {
                        float temperature = Float.parseFloat(data); // Convert received data to float
                        System.out.println("Received Temperature: " + temperature + "°C");
                        storeTemperature(conn, temperature); // Store data in MySQL
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid data received: " + data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            comPort.closePort();
        }
    }

    private static void storeTemperature(Connection conn, float temperature) {
        String sql = "INSERT INTO temperature (timestamp, temperature) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, LocalDateTime.now()); // Current timestamp
            pstmt.setFloat(2, temperature);
            pstmt.executeUpdate();
            System.out.println("Stored in DB: " + temperature + "°C");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
