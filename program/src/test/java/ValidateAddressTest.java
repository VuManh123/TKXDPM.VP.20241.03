import isd.aims.main.controller.PlaceOrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidateAddressTest {

    private final String address;
    private final boolean expected;
    private final PlaceOrderController placeOrderController = new PlaceOrderController();

    public ValidateAddressTest(String address, boolean expected) {
        this.address = address;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"123 Main St", true},          // Valid address
                {"456 Maple Ave, Apt 3", true}, // Valid with special characters
                {"", false},                    // Empty string
                {" ", false},                   // Only space
                {"Main", false},                // Too short
                {"12345 Elm Street, Apartment 9B, Springfield, Illinois 62704", true}, // Valid long address
                {"@", false},                   // Invalid special character
                {"123!! Street", false},        // Invalid special characters
                {"123 Main St  ", false},       // Trailing spaces
                {" 123 Main St", false},        // Leading spaces
                {"123--Main St", false},        // Consecutive invalid characters
                {"123 Main St, Springfield,", false}, // Trailing comma
                {"123 Main St.", false},        // Trailing period
                {null, false}                   // Null input
        });
    }

    @Test
    public void test() {
        boolean isValid = placeOrderController.validateAddress(address);
        assertEquals(expected, isValid);
    }
}
