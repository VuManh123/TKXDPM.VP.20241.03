package isd.aims.main.views.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.views.popup.PopupForm;
import javafx.scene.control.RadioButton;

import isd.aims.main.exception.InvalidDeliveryInfoException;
import isd.aims.main.controller.PlaceOrderController;
import isd.aims.main.entity.invoice.Invoice;
import isd.aims.main.entity.order.Order;
import isd.aims.main.utils.Configs;
import isd.aims.main.views.BaseForm;
import isd.aims.main.views.invoice.InvoiceForm;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class DeliveryForm extends BaseForm implements Initializable {

	@FXML
	private Label screenTitle;

	@FXML
	private Button btnBack;

	@FXML
	private Label rushlabel;
	@FXML
	private Label shippingtimelabel;

	@FXML
	private DatePicker timetextfield;

//	@FXML
//	private ImageView aimsImage;

	@FXML
	private TextField name;

	@FXML
	private TextField phone;

	@FXML
	private TextField address;


	@FXML
	private RadioButton rushbutton;

	@FXML
	private TextField rushtext;

	@FXML
	private ComboBox<String> province;


	@FXML
	private Button btnConfirmDelivery;

	@FXML
	private TextField email;

	@FXML
	private Label screenTitle1;



	private Order order;

	public DeliveryForm(Stage stage, String screenPath, Order order) throws IOException {
		super(stage, screenPath);
		this.order = order;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
		name.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
			if(newValue && firstTime.get()){
				content.requestFocus(); // Delegate the focus to container
				firstTime.setValue(false); // Variable value changed for future references

			}
		});
		this.province.getItems().addAll(Configs.PROVINCES);
		// on mouse clicked, we back to home
		btnBack.setOnMouseClicked(e -> {
			homeScreenHandler.show();
		});
		//set rushtextfield visiblle
		rushtext.setVisible(false);
		rushlabel.setVisible(false);
		shippingtimelabel.setVisible(false);
		timetextfield.setVisible(false);

		//event rushbutton
		rushbutton.setOnAction(event -> {
			boolean isSelected = rushbutton.isSelected();
			rushlabel.setVisible(isSelected);
			shippingtimelabel.setVisible(isSelected);
			timetextfield.setVisible(isSelected);
			rushtext.setVisible(isSelected);
		});

	}

	@FXML
	void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

		// add info to messages
		HashMap<String, String> messages = new HashMap<>();
		messages.put("name", name.getText());
		messages.put("phone", phone.getText());
		messages.put("address", address.getText());
		messages.put("email", email.getText());
		messages.put("province", province.getValue());
		messages.put("rushtext", rushtext.getText());
		if (timetextfield.getValue() != null) {
			messages.put("time", timetextfield.getValue().toString());
		} else {
			LocalDate currentDatePlus5 = LocalDate.now().plusDays(5);
			messages.put("time", currentDatePlus5.toString());
		}

		// Kiểm tra nếu rushButton được chọn và tỉnh không phải Hà Nội
		if (rushbutton.isSelected() && !province.getValue().equals("Hà Nội")) {
			System.out.println("Không hỗ trợ đặt hàng nhanh ở thành phố của bạn");
			PopupForm.error("Đặt hàng nhanh chỉ thực hiện ở nội thành Hà Nội");
			return;
		}

		if (rushbutton.isSelected()) {
			List<CartMedia> cartItems = Cart.getCart().getListMedia();

			// Danh sách các sản phẩm không hỗ trợ rush order
			List<String> unsupportedProducts = new ArrayList<>();

			// Duyệt qua danh sách sản phẩm
			for (CartMedia cartMedia : cartItems) {
				// Kiểm tra nếu sản phẩm không hỗ trợ rush order
				if (cartMedia.getSupportRushOrder() == 0) {
					// Thêm tên sản phẩm vào danh sách không hỗ trợ
					unsupportedProducts.add(cartMedia.getMedia().getTitle());
				}
			}

			// Kiểm tra nếu có sản phẩm không hỗ trợ rush order
			if (!unsupportedProducts.isEmpty()) {
				// In danh sách sản phẩm không hỗ trợ
				System.out.println("Sản phẩm không hỗ trợ rush order: " + String.join(", ", unsupportedProducts));

				// Hiển thị thông báo lỗi
				PopupForm.error("Các sản phẩm sau không hỗ trợ đặt hàng nhanh:\n" + String.join("\n", unsupportedProducts));

				// Dừng xử lý tiếp
				return;
			}
		}


		try {
			// process and validate delivery info
			getBController().processDeliveryInfo(messages);
		} catch (InvalidDeliveryInfoException e) {
			throw new InvalidDeliveryInfoException(e.getMessage());
		}

		// calculate shipping fees
		int shippingFees;
		if (rushbutton.isSelected()) {
			shippingFees = getBController().calculateShippingFeeRushOrder(order);
		} else {
			shippingFees = getBController().calculateShippingFee(order);
		}

		order.setShippingFees(shippingFees);
		order.setDeliveryInfo(messages);
		System.out.println(Order.getInstance().getDeliveryInfo());

		// create invoice screen
		Invoice invoice = getBController().createInvoice(order);
		BaseForm InvoiceScreenHandler = new InvoiceForm(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}

	public PlaceOrderController getBController(){
		return (PlaceOrderController) super.getBController();
	}

	public void notifyError(){
		// TODO: implement later on if we need
	}

}