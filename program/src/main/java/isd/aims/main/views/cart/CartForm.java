package isd.aims.main.views.cart;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import isd.aims.main.exception.MediaNotAvailableException;
import isd.aims.main.exception.PlaceOrderException;
import isd.aims.main.controller.PlaceOrderController;
import isd.aims.main.controller.ViewCartController;
import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.order.Order;
import isd.aims.main.utils.Configs;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.BaseForm;
import isd.aims.main.views.popup.PopupForm;
import isd.aims.main.views.shipping.DeliveryForm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CartForm extends BaseForm {

	private static Logger LOGGER = Utils.getLogger(CartForm.class.getName());

//	@FXML
//	private ImageView aimsImage;

	@FXML
	private Label pageTitle;
	@FXML
	private Button btnBack;

	@FXML
	VBox vboxCart;

	@FXML
	private Label shippingFees;

	@FXML
	private Label labelAmount;

	@FXML
	private Label labelSubtotal;

	@FXML
	private Label labelVAT;

	@FXML
	private Button btnPlaceOrder;

	@FXML
	private Label subtotal;



	public CartForm(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);

		// fix relative image path caused by fxml
//		File file = new File("isd/aims/main/fxml/images/Logo.png");
//		Image im = new Image(file.toURI().toString());
//		aimsImage.setImage(im);

		// on mouse clicked, we back to home
//		aimsImage.setOnMouseClicked(e -> {
		btnBack.setOnMouseClicked(e -> {
			homeScreenHandler.show();
		});

		// on mouse clicked, we start processing place order usecase
		btnPlaceOrder.setOnMouseClicked(e -> {
			LOGGER.info("Place Order button clicked");
			try {
				requestToPlaceOrder();
			} catch (SQLException | IOException exp) {
				LOGGER.severe("Cannot place the order, see the logs");
				exp.printStackTrace();
				throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}

		});
	}

	public Label getLabelAmount() {
		return labelAmount;
	}

	public Label getLabelSubtotal() {
		return labelSubtotal;
	}

	public ViewCartController getBController(){
		return (ViewCartController) super.getBController();
	}

	public void requestToViewCart(BaseForm prevScreen) throws SQLException {
		setPreviousScreen(prevScreen);
		setScreenTitle("Cart Screen");
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
		show();
	}

	public void requestToPlaceOrder() throws SQLException, IOException {
		try {
			// create placeOrderController and process the order
			PlaceOrderController placeOrderController = new PlaceOrderController();
			if (placeOrderController.getListCartMedia().isEmpty()){
				PopupForm.error("You don't have anything to place");
				return;
			}

			placeOrderController.placeOrder();

			// display available media
			displayCartWithMediaAvailability();

			// create order
			Order order = placeOrderController.createOrder();

			// display shipping form
			DeliveryForm DeliveryFormHandler = new DeliveryForm(this.stage, Configs.SHIPPING_SCREEN_PATH, order);
			DeliveryFormHandler.setPreviousScreen(this);
			DeliveryFormHandler.setHomeScreenHandler(homeScreenHandler);
			DeliveryFormHandler.setScreenTitle("Shipping Screen");
			DeliveryFormHandler.setBController(placeOrderController);
			DeliveryFormHandler.show();

		} catch (MediaNotAvailableException e) {
			// if some media are not available then display cart and break usecase Place Order
			displayCartWithMediaAvailability();
		}
	}

	public void updateCart() throws SQLException{
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
	}

	void updateCartAmount(){
		// calculate subtotal and amount
		int subtotal = getBController().getCartSubtotal();
		int vat = (int)((Configs.PERCENT_VAT/100)*subtotal);
		int amount = subtotal + vat;
		LOGGER.info("amount: " + amount);

		// update subtotal and amount of Cart
		labelSubtotal.setText(Utils.getCurrencyFormat(subtotal));
		labelVAT.setText(Utils.getCurrencyFormat(vat));
		labelAmount.setText(Utils.getCurrencyFormat(amount));
	}

	private void displayCartWithMediaAvailability(){
		// clear all old cartMedia
		vboxCart.getChildren().clear();

		// get list media of cart after check availability
		List lstMedia = getBController().getListCartMedia();
		LOGGER.info("Cart media count: " + lstMedia.size());

		try {
			for (Object cm : lstMedia) {
				CartMedia cartMedia = (CartMedia) cm;

				// Log thông tin về cartMedia trước khi thêm vào
				LOGGER.info("Media title: " + cartMedia.getMedia().getTitle());
				LOGGER.info("Media image: " + cartMedia.getMedia().getImageURL());
				LOGGER.info("Media price: " + cartMedia.getPrice());
				LOGGER.info("Media quantity: " + cartMedia.getQuantity());

				// Create MediaForm and set cart media
				MediaForm mediaCartScreen = new MediaForm(Configs.CART_MEDIA_PATH, this);
				mediaCartScreen.setCartMedia(cartMedia);

				// Add spinner and media form content to vboxCart
				vboxCart.getChildren().add(mediaCartScreen.getContent());
			}

			// calculate subtotal and amount
			updateCartAmount();

			LOGGER.info("Finished adding media to cart.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}