package isd.aims.main.views.mediadetail;

import isd.aims.main.controller.MediaDetailController;
import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.cart.CartMedia;
import isd.aims.main.entity.media.Media;
import isd.aims.main.exception.MediaNotAvailableException;
import isd.aims.main.utils.Configs;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.BaseForm;
import isd.aims.main.views.cart.CartForm;
import isd.aims.main.views.popup.PopupForm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MediaDetailForm extends BaseForm {
    private static Logger LOGGER = Utils.getLogger(CartForm.class.getName());

    @FXML
    private ImageView mediaImage;

    @FXML
    private Label mediaTitle;

    @FXML
    private Label mediaAvail;

    @FXML
    private Label mediaPrice;

    @FXML
    private Label mediaType;

    @FXML
    private Label mediaCategory;

    @FXML
    private Button btnBack;

    @FXML
    private Button addToCartBtn;

    @FXML
    protected Spinner<Integer> spinnerChangeNumber;

    public MediaDetailForm(Stage stage, String screenPath, Media media) throws IOException, SQLException {
        super(stage, screenPath);
        this.setInformation(media);

        btnBack.setOnMouseClicked(e -> {
            LOGGER.info("Back to home");
            getPreviousScreen().show();
        });

        addToCartBtn.setOnMouseClicked(event -> {
            try {
                if (spinnerChangeNumber.getValue() > media.getQuantity()) throw new MediaNotAvailableException();
                Cart cart = Cart.getCart();
                // if media already in cart then we will increase the quantity by 1 instead of create the new cartMedia
                CartMedia mediaInCart = this.homeScreenHandler.getBController().checkMediaInCart(media);
                if (mediaInCart != null) {
                    mediaInCart.setQuantity(mediaInCart.getQuantity() + 1);
                }else{
                    CartMedia cartMedia = new CartMedia(media, cart, spinnerChangeNumber.getValue(), media.getPrice());
                    cart.getListMedia().add(cartMedia);
                    LOGGER.info("Added " + cartMedia.getQuantity() + " " + media.getTitle() + " to cart");
                }

                // subtract the quantity and redisplay
                LOGGER.info("media.getQuantity(): " + media.getQuantity());
                LOGGER.info("spinnerChangeNumber.getValue(): " + spinnerChangeNumber.getValue());
                media.setQuantity(media.getQuantity() - spinnerChangeNumber.getValue());
                mediaAvail.setText("Available: " + String.valueOf(media.getQuantity()));
                homeScreenHandler.getNumMediaCartLabel().setText(String.valueOf(cart.getTotalMedia() + " media"));
                PopupForm.success("The media " + media.getTitle() + " added to Cart");
            } catch (MediaNotAvailableException exp) {
                try {
                    String message = "Not enough media:\nRequired: " + spinnerChangeNumber.getValue() + "\nAvail: " + media.getQuantity();
                    LOGGER.severe(message);
                    PopupForm.error(message);
                } catch (Exception e) {
                    LOGGER.severe("Cannot add media to cart: ");
                }

            } catch (Exception exp) {
                LOGGER.severe("Cannot add media to cart: ");
                exp.printStackTrace();
            }
        });
    }

    protected void setInformation(Media media) throws SQLException {
        this.mediaTitle.setText(media.getTitle());
        this.mediaAvail.setText("Available: " + Integer.toString(media.getQuantity()));
        this.mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        this.mediaType.setText("Type: " + media.getType());
        this.mediaCategory.setText("Category: " + media.getCategory());

        File file = new File(Configs.IMAGE_PATH + media.getImageURL());
        Image image = new Image(file.toURI().toString());
        mediaImage.setImage(image);

            spinnerChangeNumber.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1)
        );
    }

    public MediaDetailController getBController(){
        return (MediaDetailController) super.getBController();
    }

    public void requestToViewDetailMedia(BaseForm prevScreen){
        setPreviousScreen(prevScreen);
        setScreenTitle("Media Detail");
        show();
    }
}
