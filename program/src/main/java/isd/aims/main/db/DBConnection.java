package isd.aims.main.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.sql.Connection;
import isd.aims.main.utils.Utils;

public class DBConnection {

    private static Logger LOGGER = Utils.getLogger(DBConnection.class.getName());
    private static Connection connect;

    public static Connection getConnection() {
        if (connect != null) {
            try {
                // Kiểm tra nếu kết nối đã đóng
                if (connect.isClosed()) {
                    connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3308/aims", "root", "manhvu123");
                }
            } catch (SQLException e) {
                LOGGER.severe("Error checking if connection is closed: " + e.getMessage());
            }
        } else {
            try {
                connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3308/aims", "root", "manhvu123");
            } catch (SQLException e) {
                LOGGER.severe("Unable to connect to the database: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connect;
    }

    public static void closeConnection() {
        try {
            if (connect != null && !connect.isClosed()) {
                connect.close();
            }
        } catch (SQLException e) {
            LOGGER.severe("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getConnection();
        closeConnection();
    }
}

