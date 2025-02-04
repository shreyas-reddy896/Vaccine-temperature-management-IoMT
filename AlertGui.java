import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class AlertGui {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sensor_data";
    private static final String DB_USER = "your_username";
    private static final String DB_PASS = "your_password";
    private static final float LOWER_THRESHOLD = 2.0f;
    private static final float UPPER_THRESHOLD = 8.0f;
    
    private JLabel alertLabel;

    public AlertGui() {
        JFrame frame = new JFrame("Temperature Alerts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        
        alertLabel = new JLabel("Monitoring temperature...", SwingConstants.CENTER);
        frame.add(alertLabel, BorderLayout.CENTER);
        
        frame.setVisible(true);
        startMonitoring();
    }

    private void startMonitoring() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkTemperature();
            }
        }, 0, 60000); // Check every 60 seconds
    }

    private void checkTemperature() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT temperature, timestamp FROM temperature ORDER BY id DESC LIMIT 1")) {
            
            if (rs.next()) {
                float temp = rs.getFloat("temperature");
                if (temp < LOWER_THRESHOLD) {
                    alertLabel.setText("ALERT: Temperature too low! " + temp + "°C");
                } else if (temp > UPPER_THRESHOLD) {
                    alertLabel.setText("ALERT: Temperature too high! " + temp + "°C");
                } else {
                    alertLabel.setText("Temperature Normal: " + temp + "°C");
                }
            }
        } catch (SQLException e) {
            alertLabel.setText("Database Error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AlertGui::new);
    }
}
