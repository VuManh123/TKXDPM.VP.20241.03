package isd.aims.main.views.home;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import isd.aims.main.controller.MediaDetailController;
import isd.aims.main.entity.media.Media;
import isd.aims.main.utils.Configs;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.FXMLForm;
import isd.aims.main.views.mediadetail.MediaDetailForm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MediaForm extends FXMLForm {
    @FXML
    protected ImageView mediaImage;

    @FXML
    protected Label mediaTitle;

    @FXML
    protected Label mediaPrice;

    @FXML
    protected Label mediaAvail;

    @FXML
    protected Spinner<Integer> spinnerChangeNumber;

    @FXML
    protected Button addToCartBtn;

    private static Logger LOGGER = Utils.getLogger(MediaForm.class.getName());
    private Media media;
    private HomeForm home;
    private Stage stage;
    @FXML
    private void handleVBoxClick(MouseEvent event) {
        try {
            LOGGER.info("User clicked to view detail of media: " + media.getTitle());
            MediaDetailForm mediaDetailScreen = new MediaDetailForm(this.home.getStage(),Configs.MEDIA_DETAIL_PATH,media);
            LOGGER.info("check media: " + media);
            mediaDetailScreen.setBController(new MediaDetailController());
            mediaDetailScreen.setHomeScreenHandler(this.home);
            mediaDetailScreen.requestToViewDetailMedia(this.home);
        } catch (IOException e1) {
            LOGGER.severe("Error occurred while opening media detail page: " + e1.getMessage());
            e1.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public MediaForm(String screenPath, Media media, HomeForm home) throws SQLException, IOException{
        super(screenPath);
        this.media = media;
        this.home = home;

//        addToCartBtn.setOnMouseClicked(event -> {
//            try {
//                if (spinnerChangeNumber.getValue() > media.getQuantity()) throw new MediaNotAvailableException();
//                Cart cart = Cart.getCart();
//                // if media already in cart then we will increase the quantity by 1 instead of create the new cartMedia
//                CartMedia mediaInCart = home.getBController().checkMediaInCart(media);
//                if (mediaInCart != null) {
//                    mediaInCart.setQuantity(mediaInCart.getQuantity() + 1);
//                }else{
//                    CartMedia cartMedia = new CartMedia(media, cart, spinnerChangeNumber.getValue(), media.getPrice());
//                    cart.getListMedia().add(cartMedia);
//                    LOGGER.info("Added " + cartMedia.getQuantity() + " " + media.getTitle() + " to cart");
//                }
//
//                // subtract the quantity and redisplay
//                media.setQuantity(media.getQuantity() - spinnerChangeNumber.getValue());
//                mediaAvail.setText(String.valueOf(media.getQuantity()));
//                home.getNumMediaCartLabel().setText(String.valueOf(cart.getTotalMedia() + " media"));
//                PopupForm.success("The media " + media.getTitle() + " added to Cart");
//            } catch (MediaNotAvailableException exp) {
//                try {
//                    String message = "Not enough media:\nRequired: " + spinnerChangeNumber.getValue() + "\nAvail: " + media.getQuantity();
//                    LOGGER.severe(message);
//                    PopupForm.error(message);
//                } catch (Exception e) {
//                    LOGGER.severe("Cannot add media to cart: ");
//                }
//
//            } catch (Exception exp) {
//                LOGGER.severe("Cannot add media to cart: ");
//                exp.printStackTrace();
//            }
//        });
        setMediaInfo();
    }

    public Media getMedia(){
        return media;
    }

    private void setMediaInfo() throws SQLException {
        File file = new File(Configs.IMAGE_PATH + media.getImageURL());
        Image image = new Image(file.toURI().toString());
        mediaImage.setFitHeight(160);
        mediaImage.setFitWidth(152);
        mediaImage.setImage(image);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
    }

}
