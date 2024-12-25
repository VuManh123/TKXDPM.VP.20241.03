package isd.aims.main.dao;

import isd.aims.main.db.DBConnection;
import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.media.Media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    public void insertCartMedia(int cartID, int mediaID, int numberOfProducts) throws SQLException {
        String query = "INSERT INTO cart_media (cart_id, media_id, numberOf_products) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, cartID);
            ps.setInt(2, mediaID);
            ps.setInt(3, numberOfProducts);
            ps.executeUpdate();
        }
    }

    public List<CartMedia> getCartMedia(int cartID) throws SQLException {
        String query = "SELECT cm.media_id, cm.number_of_products, m.type, m.category, m.price, m.quantity, m.title, m.value, m.imageUrl, m.support_for_rush_delivery " +
                "FROM cart_media cm " +
                "JOIN media m ON cm.media_id = m.id " +
                "WHERE cm.cart_id = ?";
        List<CartMedia> cartItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Media media = new Media(
                            rs.getInt("media_id"),
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getInt("value"),
                            rs.getInt("price"),
                            rs.getInt("quantity"),
                            rs.getString("type"),
                            rs.getString("imageUrl")
                    );
                    CartMedia cartItem = new CartMedia(media, rs.getInt("number_of_products"), rs.getInt("number_of_products") * rs.getInt("price"));
                    cartItems.add(cartItem);
                }
            }
            return cartItems;
        }
    }
}
