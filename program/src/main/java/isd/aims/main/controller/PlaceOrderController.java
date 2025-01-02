package isd.aims.main.controller;

import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.invoice.Invoice;
import isd.aims.main.entity.order.Order;
import isd.aims.main.entity.order.OrderMedia;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.popup.PopupForm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = Utils.getLogger(PlaceOrderController.class.getName());


    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    @SuppressWarnings("unchecked")
    public Order createOrder() throws SQLException {
        Order order = Order.getInstance();

        // Generate a unique order ID
        int uniqueOrderID;
        do {
            uniqueOrderID = Order.generateOrderID();
        } while (Order.isOrderIDExist(String.valueOf(uniqueOrderID)));

        order.setId(uniqueOrderID);

        // Copy items from cart to order
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice());
            order.getlstOrderMedia().add(orderMedia);
        }
        System.out.println(order);
        return order;
    }



    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    @SuppressWarnings("rawtypes")
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }


    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
        if (info == null || info.isEmpty()) {
            PopupForm.error("Delivery information cannot be null or empty.");
            return;
        }

        String name = info.get("name");
        String address = info.get("address");
        String phoneNumber = info.get("phone");
        String email = info.get("email");

        PlaceOrderController controller = new PlaceOrderController();

        if (name == null || name.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                phoneNumber == null || phoneNumber.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
            PopupForm.error("Please fill all information!");
            throw new IllegalArgumentException("Please fill all information");
        }

        // Validate name
        if (!controller.validateName(name)) {
            PopupForm.error("Invalid name: " + name);
            throw new IllegalArgumentException("Invalid name: " + name);
        }

        // Validate address
        if (!controller.validateAddress(address)) {
            PopupForm.error("Invalid address: " + address);
            throw new IllegalArgumentException("Invalid address: " + address);
        }

        // Validate phone number
        if (!controller.validatePhoneNumber(phoneNumber)) {
            PopupForm.error("Invalid phone number: " + phoneNumber);
            throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
        }

        // Validate email
        if (!validateEmail(email)) {
            PopupForm.error("Invalid email: " + email);
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    private boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Basic regex for email validation
        String emailRegex = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }


    public boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        // Remove spaces and dashes to validate the core number
        String normalizedPhone = phoneNumber.replaceAll("[ -]", "");

        // Check if the normalized number starts with '0' and has exactly 10 digits
        if (!normalizedPhone.matches("0\\d{9}")) {
            return false;
        }

        // Ensure no space or dash at the start or end
        if (phoneNumber.startsWith(" ") || phoneNumber.startsWith("-") ||
                phoneNumber.endsWith(" ") || phoneNumber.endsWith("-")) {
            return false;
        }

        // Ensure separators are not repeated (e.g., "0123--4567" is invalid)
        if (phoneNumber.contains("--") || phoneNumber.contains("  ")) {
            return false;
        }

        return true;
    }

    public boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // Check length (between 2 and 50 characters)
        if (name.length() < 2 || name.length() > 30) {
            return false;
        }

        // Ensure the name contains only letters and spaces
        if (!name.matches("[a-zA-Z ]+")) {
            return false;
        }

        // No consecutive spaces allowed
        if (name.contains("  ")) {
            return false;
        }

        return true;
    }

    public boolean validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }

        // Check length (between 5 and 100 characters)
        if (address.length() < 5 || address.length() > 100) {
            return false;
        }

        // Allow letters, digits, spaces, and certain special characters (, . - /)
        if (!address.matches("[a-zA-Z0-9,\\.\\-/ ]+")) {
            return false;
        }

        // No consecutive spaces allowed
        if (address.contains("  ")) {
            return false;
        }

        // Ensure no special characters or spaces at the start or end
        if (address.startsWith(" ") || address.endsWith(" ") ||
                address.startsWith(",") || address.endsWith(",") ||
                address.startsWith(".") || address.endsWith(".") ||
                address.startsWith("-") || address.endsWith("-") ||
                address.startsWith("/") || address.endsWith("/")) {
            return false;
        }

        return true;
    }


    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order) {
        int quantity = order.getQuantityCart();
        int shippingFee = 0;

        for (int i = 1; i <= quantity; i++) {
            if (i == 1) {
                shippingFee += 5; // Phí ship cho sản phẩm đầu tiên
            } else if (i == 2) {
                shippingFee += 4; // Phí ship cho sản phẩm thứ hai
            } else {
                shippingFee += 3; // Phí ship giảm dần cho các sản phẩm tiếp theo
            }
        }
        return shippingFee;
    }

    public int calculateShippingFeeRushOrder(Order order) {
        int quantity = order.getQuantityCart();
        int shippingFee = 0;

        for (int i = 1; i <= quantity; i++) {
            if (i == 1) {
                shippingFee += 12; // Phí ship nhanh cho sản phẩm đầu tiên
            } else if (i == 2) {
                shippingFee += 10; // Phí ship nhanh cho sản phẩm thứ hai
            } else {
                shippingFee += 8; // Phí ship nhanh giảm dần cho các sản phẩm tiếp theo
            }
        }
        return shippingFee;
    }

}
