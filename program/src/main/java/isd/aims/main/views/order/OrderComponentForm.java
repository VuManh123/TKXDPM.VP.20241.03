package isd.aims.main.views.order;

import isd.aims.main.entity.order.Order;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.FXMLForm;
import isd.aims.main.views.home.HomeForm;
import isd.aims.main.views.home.MediaForm;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class OrderComponentForm extends FXMLForm {
    @FXML
    protected Label orderId;

    @FXML
    protected Label orderTime;

    private static Logger LOGGER = Utils.getLogger(MediaForm.class.getName());
    private Order order;
    private HomeForm home;
    private OrderForm orderForm;
    private Stage stage;
    @FXML
    private void handleVBoxClick(MouseEvent event) {
        LOGGER.info("User clicked to view detail of order: " + order.getId());
        orderForm.showOrderDetails(this.order);
    }

    public OrderComponentForm(String screenPath, Order order, HomeForm home, OrderForm orderForm) throws SQLException, IOException{
        super(screenPath);
        this.order = order;
        this.home = home;
        this.orderForm = orderForm;
        setMediaInfo();
    }

    public Order getOrder(){
        return order;
    }

    private void setMediaInfo() throws SQLException {
        orderId.setText(order.getId().toString());
//        orderTime.setText(Utils.getCurrencyFormat(media.getPrice()));
    }
}
