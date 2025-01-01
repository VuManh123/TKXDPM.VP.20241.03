import isd.aims.main.controller.PlaceOrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidateNameTest {

    private final String name;
    private final boolean expected;
    private final PlaceOrderController placeOrderController = new PlaceOrderController();

    public ValidateNameTest(String name, boolean expected) {
        this.name = name;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"John", true},             // Valid name
                {"Mary Ann", true},         // Valid with space
                {"Mary-Ann", false},        // Invalid due to hyphen
                {"", false},                // Empty string
                {" ", false},               // Only space
                {"Jo", true},               // Valid minimum length
                {"J", false},               // Too short
                {"Johnathan Alexander the Third", true}, // Valid long name
                {"John123", false},         // Contains numbers
                {"John@Doe", false},        // Contains special character
                {"John   Doe", true},       // Consecutive spaces
                {"JohnDoeIsAReallyLongNameThatExceedsFiftyCharacters", false}, // Too long
                {"Nguyễn Văn A", false},    // Contains non-English characters
                {null, false}               // Null input
        });
    }

    @Test
    public void test() {
        boolean isValid = placeOrderController.validateName(name);
        assertEquals(expected, isValid);
    }
}
