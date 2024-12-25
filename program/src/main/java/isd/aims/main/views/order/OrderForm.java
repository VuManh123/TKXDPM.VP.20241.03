package isd.aims.main.views.order;

import isd.aims.main.controller.OrderController;
import isd.aims.main.controller.ViewCartController;
import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.media.Media;
import isd.aims.main.entity.order.Order;
import isd.aims.main.exception.ViewCartException;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.BaseForm;
import isd.aims.main.views.cart.CartForm;
import isd.aims.main.utils.Configs;
import isd.aims.main.views.home.MediaForm;
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
            LOGGER.info("Send request check order");
            LOGGER.info("Check orderId: " + inputOrderId.getText());
            LOGGER.info("Check email: " + inputEmail.getText());
            verifyArea.setVisible(true);
        });
        btnVerify.setOnMouseClicked(e -> {
            LOGGER.info("Send request verify");
            LOGGER.info("Check verify code: " + inputVerifyCode.getText());
            inputOrderId.setText("");
            inputEmail.setText("");
            inputVerifyCode.setText("");
            verifyArea.setVisible(false);
            // Can sua cai nay de lay san pham trong gio hang
            List medium = null;
            try {
                medium = getBController().getAllMedia();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            this.orderItems = new ArrayList();
            for (Object object : medium) {
                Media media = (Media) object;
                OrderDetailMediaForm m1 = null;
                try {
                    m1 = new OrderDetailMediaForm(Configs.ORDER_DETAIL_MEDIA_PATH, media, this.homeScreenHandler);
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

//    private List<Order> getMockOrders() {
//        List<Order> orders = new ArrayList<>();
//        orders.add(new Order(1, 5));
//        orders.add(new Order(2, 7));
//        orders.add(new Order(3, 10));
//        return orders;
//    }
    @Override
    public void show(){
        try {
//            // Tạo danh sách orders giả lập
//            var orders = getMockOrders();
//
//            // Clear nội dung cũ
//            listOrder.getChildren().clear();
//
//            // Duyệt qua từng order và thêm vào listOrder
//            for (Order order : orders) {
//                try {
//                    OrderComponentForm orderComponent = new OrderComponentForm(Configs.ORDER_COMPONENT_PATH, order, homeScreenHandler, this);
//                    listOrder.getChildren().add(orderComponent.getContent());
//                } catch (IOException | SQLException e) {
//                    LOGGER.warning("Error loading order component: " + e.getMessage());
//                }
//            }
        } catch (Exception e) {
            LOGGER.severe("Error fetching orders: " + e.getMessage());
        }

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

    public void showOrderDetails(Order order) {
        // Cập nhật các Label với thông tin chi tiết từ đơn hàng
        orderId.setText(order.getId().toString());
//        orderTime.setText(order.getOrderTime().toString());
//        orderAddress.setText(order.getAddress());
//        orderPhoneNumber.setText(order.getPhoneNumber());
//        orderEmail.setText(order.getEmail());
//        orderShippingFee.setText(order.getShippingFee().toString());
//        orderTotalPrice.setText(order.getTotalPrice().toString());
    }

    public OrderController getBController(){
        return (OrderController) super.getBController();
    }

    public void requestToViewOrder(BaseForm prevScreen){
        setPreviousScreen(prevScreen);
        setScreenTitle("Order Screen");
        show();
    }
}
