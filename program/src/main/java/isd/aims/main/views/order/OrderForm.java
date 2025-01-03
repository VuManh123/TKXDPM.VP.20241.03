package isd.aims.main.views.order;

import isd.aims.main.controller.OrderController;
import isd.aims.main.controller.ViewCartController;
import isd.aims.main.dao.InvoiceDAO;
import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.invoice.InvoiceDetails;
import isd.aims.main.entity.media.Media;
import isd.aims.main.entity.order.Order;
import isd.aims.main.entity.order.OrderProduct;
import isd.aims.main.exception.ViewCartException;
import isd.aims.main.utils.Email;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.BaseForm;
import isd.aims.main.views.cart.CartForm;
import isd.aims.main.utils.Configs;
import isd.aims.main.views.home.MediaForm;
import isd.aims.main.views.popup.PopupForm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import static java.lang.Integer.parseInt;

public class OrderForm extends BaseForm {
    private static Logger LOGGER = Utils.getLogger(CartForm.class.getName());
    @FXML
    protected Label numMediaInCart;
    @FXML
    protected Button btnBack;
    @FXML
    protected Button btnCart;
//    @FXML
//    protected VBox listOrder;
    @FXML
    protected VBox orderInput;
    @FXML
    protected VBox verifyArea;
    @FXML
    protected VBox orderDetail;
    @FXML
    protected VBox orderMediaList;
    @FXML
    protected TextField inputOrderId;
    @FXML
    protected Label mesOrderId;
    @FXML
    protected TextField inputEmail;
    @FXML
    protected Label mesEmail;
    @FXML
    protected Button btnSendRequest;
    @FXML
    protected TextField inputVerifyCode;
    @FXML
    protected Label mesVerifyCode;
    @FXML
    protected Button btnVerify;
    @FXML
    protected Label orderId;
    @FXML
    protected Label orderTime;
    @FXML
    protected Label orderAddress;
    @FXML
    protected Label orderPhoneNumber;
    @FXML
    protected Label orderEmail;
    @FXML
    protected Label orderShippingFee;
    @FXML
    protected Label orderTotalPrice;
    private List orderItems;
    private InvoiceDetails invoice;
    private List<OrderProduct> products;
    private String otp;
    public void onChangeInputOrderID() {
        if(mesOrderId.isVisible()) {
            mesOrderId.setVisible(false);
        }
    }
    public void onChangeInputEmail() {
        if(mesEmail.isVisible()) {
            mesEmail.setVisible(false);
        }
    }

    public OrderForm(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        btnBack.setOnMouseClicked(e -> {
            LOGGER.info("Back to home");
            getPreviousScreen().show();
        });

        btnCart.setOnMouseClicked(e -> {
            LOGGER.info("Go to cart");
            try {
                LOGGER.info("Go to cart");
                CartForm cartScreen = new CartForm(stage, Configs.CART_SCREEN_PATH);
                cartScreen.setHomeScreenHandler(this.homeScreenHandler);
                cartScreen.setBController(new ViewCartController());
                cartScreen.requestToViewCart(this);
            } catch (IOException | SQLException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });
        verifyArea.setVisible(false);
        mesOrderId.setVisible(false);
        mesEmail.setVisible(false);
        mesVerifyCode.setVisible(false);
        btnSendRequest.setOnMouseClicked(e -> {
            LOGGER.info("Check orderId: " + inputOrderId.getText());
            LOGGER.info("Check email: " + inputEmail.getText());
            if (inputOrderId.getText().isEmpty()) {
                mesEmail.setVisible(false);
                mesOrderId.setText("Please enter orderID");
                mesOrderId.setVisible(true);
            } else if (inputEmail.getText().isEmpty()) {
                mesOrderId.setVisible(false);
                mesEmail.setText("Please enter orderID");
                mesEmail.setVisible(true);
            } else if (!validateEmail(inputEmail.getText())) {
                mesEmail.setText("Email Invalid");
                mesEmail.setVisible(true);
            } else {
                InvoiceDAO invoiceDAO = new InvoiceDAO();
                invoice = invoiceDAO.getInvoiceDetails(parseInt(inputOrderId.getText()));
                products = invoiceDAO.getOrderProducts(parseInt(inputOrderId.getText()));
                System.out.println(products);
                if (invoice == null) {
                    try {
                        PopupForm.error("OrderID isn't existed!");
                        return;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (!invoice.getEmail().equals(inputEmail.getText())) {
                    try {
                        PopupForm.error("Email isn't match to email of order!");
                        return;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                mesEmail.setVisible(false);
                mesOrderId.setVisible(false);
                verifyArea.setVisible(true);

                String senderEmail = "devvu203@gmail.com";
                String senderPassword = "zzgy xrxc clro fxpx";
                Email email = new Email(senderEmail, senderPassword);
                otp = email.generateOtp(6);
                email.sendOtp(inputEmail.getText(), otp);
            }

        });
        btnVerify.setOnMouseClicked(e -> {
            LOGGER.info("Send request verify");
            LOGGER.info("Check verify code: " + inputVerifyCode.getText());
            showOrderDetails();
            verifyArea.setVisible(false);
            if (inputVerifyCode.getText().isEmpty()) {
                mesVerifyCode.setText("Please fill verify code!");
                mesVerifyCode.setVisible(true);
            }
            System.out.println(otp);
            if (!inputVerifyCode.getText().equals(otp)) {
                try {
                    PopupForm.error("OTP Code isn't matched!");
                    return;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            inputOrderId.setText("");
            inputEmail.setText("");
            inputVerifyCode.setText("");

            List medium;
            try {
                medium = getBController().getAllMedia();
                System.out.println(medium);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            this.orderItems = new ArrayList();
            for (OrderProduct object : products) {
                OrderDetailMediaForm m1 = null;
                try {
                    m1 = new OrderDetailMediaForm(Configs.ORDER_DETAIL_MEDIA_PATH, object, this.homeScreenHandler);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                this.orderItems.add(m1);
            }
            addMediaOrder(this.orderItems);

        });
    }

    @Override
    public void show(){
        // Hiển thị số lượng media trong giỏ hàng
        int cartSize = Cart.getCart().getListMedia().size();
        if (cartSize == 0) {
            numMediaInCart.setVisible(false);
            numMediaInCart.setManaged(false);
        } else {
            numMediaInCart.setVisible(true);
            numMediaInCart.setManaged(true);
            numMediaInCart.setText(String.valueOf(cartSize));
        }
        super.show();
    }

    @SuppressWarnings("rawtypes")
    public void addMediaOrder(List items){
        LOGGER.info("check list item: "+ items);
        ArrayList mediaItems = (ArrayList)((ArrayList) items).clone();
        orderMediaList.getChildren().clear();
        while(!mediaItems.isEmpty()){
            OrderDetailMediaForm media = (OrderDetailMediaForm) mediaItems.get(0);
            orderMediaList.getChildren().add(media.getContent());
            mediaItems.remove(media);
        }
    }

    public void showOrderDetails() {
        // Cập nhật các Label với thông tin chi tiết từ đơn hàng
        orderId.setText(String.valueOf(invoice.getOrderID()));
        orderTime.setText(invoice.getDate().toString());
        orderAddress.setText(invoice.getAddress());
        orderPhoneNumber.setText(invoice.getPhoneNumber());
        orderEmail.setText(invoice.getEmail());
        orderShippingFee.setText(String.valueOf(invoice.getShippingFee()));
        orderTotalPrice.setText(String.valueOf(invoice.getTotal()));
    }

    public OrderController getBController(){
        return (OrderController) super.getBController();
    }

    public void requestToViewOrder(BaseForm prevScreen){
        setPreviousScreen(prevScreen);
        setScreenTitle("Order Screen");
        show();
    }

    private boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Basic regex for email validation
        String emailRegex = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}
