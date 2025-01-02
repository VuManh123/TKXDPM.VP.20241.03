package isd.aims.main.entity.order;

import isd.aims.main.db.DBConnection;
import isd.aims.main.utils.Configs;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Order {

    private int shippingFees;
    private List lstOrderMedia;
    private HashMap<String, String> deliveryInfo;
    private Integer id;

    private static Order instance;

    public static Order getInstance() {
        if (instance == null) {
            instance = new Order();
        }
        return instance;
    }


    public Order(){
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public Order(int id, int shippingFees){
        this.id = id;
        this.shippingFees = shippingFees;
    }

    public void addOrderMedia(OrderMedia om){
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.lstOrderMedia.remove(om);
    }

    public List getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public HashMap getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(HashMap deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }
    public void emptyOrder() {
        this.id = null; // Xóa ID của đơn hàng
        this.shippingFees = 0; // Reset phí vận chuyển
        this.lstOrderMedia.clear(); // Xóa danh sách sản phẩm
        if (this.deliveryInfo != null) {
            this.deliveryInfo.clear(); // Xóa thông tin giao hàng nếu tồn tại
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice()*om.getQuantity();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }
    public int getQuantityCart(){
        int quantity = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            quantity += om.getQuantity();
        }
        return quantity;
    }
    public static int generateOrderID() {
        Random random = new Random();
        return 10000000 + random.nextInt(90000000);
    }

    public static boolean isOrderIDExist(String orderID) throws SQLException {
        String query = "SELECT COUNT(*) FROM `order` WHERE orderID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, orderID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Nếu COUNT > 0, OrderID đã tồn tại
                }
            }
        }
        return false; // Mặc định là không tồn tại
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order Details:").append("\n");
        sb.append("ID: ").append(id == null ? "Not Assigned" : id).append("\n");
        sb.append("Shipping Fees: ").append(shippingFees).append("\n");
        sb.append("Delivery Info: ").append(deliveryInfo != null ? deliveryInfo : "Not Provided").append("\n");
        sb.append("Order Media List:").append("\n");

        for (Object om : lstOrderMedia) {
            sb.append(om.toString()).append("\n");
        }
        return sb.toString();
    }
}
