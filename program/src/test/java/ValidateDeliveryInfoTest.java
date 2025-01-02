import isd.aims.main.controller.PlaceOrderController;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertThrows;

public class ValidateDeliveryInfoTest {

    private final PlaceOrderController placeOrderController = new PlaceOrderController();

    @Test
    public void testValidInfo() throws InterruptedException, IOException {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "John Doe");
        info.put("address", "123 Main St, Springfield");
        info.put("phoneNumber", "0123456789");
        info.put("email", "john.doe@example.com");

        placeOrderController.validateDeliveryInfo(info);
    }

    @Test
    public void testInvalidName() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "J"); // Invalid name
        info.put("address", "123 Main St");
        info.put("phoneNumber", "0123456789");
        info.put("email", "john.doe@example.com");

        assertThrows(IllegalArgumentException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    public void testInvalidAddress() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "John Doe");
        info.put("address", "123"); // Invalid address
        info.put("phoneNumber", "0123456789");
        info.put("email", "john.doe@example.com");

        assertThrows(IllegalArgumentException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    public void testInvalidPhoneNumber() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "John Doe");
        info.put("address", "123 Main St");
        info.put("phoneNumber", "12345"); // Invalid phone number
        info.put("email", "john.doe@example.com");

        assertThrows(IllegalArgumentException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    public void testInvalidEmail() {
        HashMap<String, String> info = new HashMap<>();
        info.put("name", "John Doe");
        info.put("address", "123 Main St");
        info.put("phoneNumber", "0123456789");
        info.put("email", "john.doe@com"); // Invalid email

        assertThrows(IllegalArgumentException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    public void testEmptyInfo() {
        HashMap<String, String> info = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> placeOrderController.validateDeliveryInfo(info));
    }

    @Test
    public void testNullInfo() {
        assertThrows(IllegalArgumentException.class, () -> placeOrderController.validateDeliveryInfo(null));
    }
}
