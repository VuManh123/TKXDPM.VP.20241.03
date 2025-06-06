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
