package isd.aims.main.dao;

import isd.aims.main.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderDAO {
    public static void insertOrder(int orderID, int total, int totalShippingFee) throws SQLException {
        String query = "INSERT INTO `order` (orderID, total, total_shipping_fee) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set các giá trị cho các tham số trong câu lệnh SQL
            statement.setInt(1, orderID);
            statement.setInt(2, total);
            statement.setInt(3, totalShippingFee);

            // Thực thi câu lệnh
            int rowsAffected = statement.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Order inserted successfully.");
            } else {
                System.out.println("Failed to insert order.");
            }
        }
    }

    public static void insertDelivery(String provinceCity, String deliveryAddress, String recipientName,
                                      String email, String phoneNumber, LocalDateTime deliveryTime,
                                      String deliveryInstructions, int orderID) throws SQLException {
        String query = """
        INSERT INTO DELIVERY 
        (province_city, delivery_address, recipient_name, email, phone_number, delivery_time, delivery_instructions, orderID) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set các giá trị cho các tham số
            statement.setString(1, provinceCity);
            statement.setString(2, deliveryAddress);
            statement.setString(3, recipientName);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            statement.setTimestamp(6, Timestamp.valueOf(deliveryTime));
            statement.setString(7, deliveryInstructions);
            statement.setInt(8, orderID);

            // Thực thi câu lệnh
            int rowsAffected = statement.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Delivery information inserted successfully.");
            } else {
                System.out.println("Failed to insert delivery information.");
            }
        }
    }

    public static void insertOrderMedia(int orderID, int mediaID, int numberOfProducts) throws SQLException {
        String query = """
        INSERT INTO ORDER_MEDIA (orderID, mediaID, number_of_products)
        VALUES (?, ?, ?)
        """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set các giá trị tham số
            statement.setInt(1, orderID);
            statement.setInt(2, mediaID);
            statement.setInt(3, numberOfProducts);

            // Thực thi câu lệnh
            int rowsAffected = statement.executeUpdate();

            // Kiểm tra kết quả
            if (rowsAffected > 0) {
                System.out.println("Order media information inserted successfully.");
            } else {
                System.out.println("Failed to insert order media information.");
            }
        }
    }
}
