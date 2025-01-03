package isd.aims.main.dao;

import isd.aims.main.db.DBConnection;
import isd.aims.main.entity.invoice.InvoiceDetails;
import isd.aims.main.entity.order.OrderProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    public InvoiceDetails getInvoiceDetails(int orderID) {
        String sql = """
            SELECT 
                o.orderID,
                pt.date,  
                CONCAT(di.delivery_address, ',', di.province_city) AS address,
                di.email,
                di.phone_number,
                o.total_shipping_fee,
                o.total
            FROM 
                `ORDER` o
            JOIN 
                PAYMENT_TRANSACTION pt ON o.orderID = pt.orderID
            JOIN 
                DELIVERY di ON o.orderID = di.orderID
            WHERE 
                o.orderID = ?;
        """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new InvoiceDetails(
                            resultSet.getInt("orderID"),
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getString("address"),
                            resultSet.getString("email"),
                            resultSet.getString("phone_number"),
                            resultSet.getDouble("total_shipping_fee"),
                            resultSet.getDouble("total")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return null;
    }

    public List<OrderProduct> getOrderProducts(int orderID) {
        String sql = """
            SELECT 
                om.orderID,
                m.title AS product_title,
                m.price AS product_price,
                m.imageUrl AS product_image,
                om.number_of_products
            FROM 
                ORDER_MEDIA om
            JOIN 
                MEDIA m ON om.mediaID = m.id
            WHERE 
                om.orderID = ?;
        """;

        List<OrderProduct> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(new OrderProduct(
                            resultSet.getInt("orderID"),
                            resultSet.getString("product_title"),
                            resultSet.getInt("product_price"),
                            resultSet.getString("product_image"),
                            resultSet.getInt("number_of_products")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return products;
    }
}
