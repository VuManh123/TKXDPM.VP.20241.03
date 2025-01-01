package isd.aims.main.views.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
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
		messages.put("province", province.getValue());
		messages.put("rushtext", rushtext.getText());
		try {
			// process and validate delivery info
			getBController().processDeliveryInfo(messages);
		} catch (InvalidDeliveryInfoException e) {
			throw new InvalidDeliveryInfoException(e.getMessage());
		}

		// calculate shipping fees
		int shippingFees = getBController().calculateShippingFee(order);
		order.setShippingFees(shippingFees);
		order.setDeliveryInfo(messages);

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