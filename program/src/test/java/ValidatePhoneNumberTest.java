import isd.aims.main.controller.PlaceOrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidatePhoneNumberTest {

    private final String phone;
    private final boolean expected;
    private final PlaceOrderController placeOrderController = new PlaceOrderController();

    public ValidatePhoneNumberTest(String phone, boolean expected) {
        this.phone = phone;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"0123456789", true},          // Valid case
                {"012-345-6789", true},        // Valid with dashes
                {"012 345 6789", true},        // Valid with spaces
                {"0-123 456-789", true},       // Valid mix of spaces and dashes
                {"1234567890", false},         // Doesn't start with 0
                {"0123--4567", false},         // Repeated dashes
                {"0123  4567", false},         // Repeated spaces
                {" 0123456789", false},        // Space at the start
                {"0123456789 ", false},        // Space at the end
                {"-0123456789", false},        // Dash at the start
                {"0123456789-", false},        // Dash at the end
                {"01234-56789", true},         // Valid single dash
                {"0123 4567890", false},       // Too long after normalization
                {"01234abc567", false},        // Contains invalid characters
                {"012 345678", false},         // Too short
                {"", false},                   // Empty string
                {null, false}                  // Null input
        });
    }

    @Test
    public void test() {
        boolean isValided = placeOrderController.validatePhoneNumber(phone);
        assertEquals(expected, isValided);
    }
}
