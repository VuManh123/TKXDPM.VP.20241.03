package isd.aims.main.views.home;

import isd.aims.main.exception.ViewCartException;
import isd.aims.main.controller.HomeController;
import isd.aims.main.controller.ViewCartController;
import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.media.Media;
import isd.aims.main.utils.Configs;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.BaseForm;
import isd.aims.main.views.cart.CartForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class HomeForm extends BaseForm implements Initializable {

    public static Logger LOGGER = Utils.getLogger(HomeForm.class.getName());

    @FXML
    private Label numMediaInCart;

    @FXML
    private ImageView aimsImage;

    @FXML
    private ImageView cartImage;


    @FXML
    private HBox hboxMedia;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;

    @FXML
    private TextField searchTextField;

    @FXML
    private RadioButton sortByPriceIncrease;

    @FXML
    private RadioButton sortByPriceDecrease;

    @FXML
    private RadioButton sortByAlphabetAZ;

    @FXML
    private RadioButton sortByAlphabetZA;

    @FXML
    private CheckBox filterBook;

    @FXML
    private CheckBox filterDVD;

    @FXML
    private CheckBox filterCD;

    @SuppressWarnings("rawtypes")
    private List homeItems;

    private List<MediaForm> filteredItems;

    public HomeForm(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public Label getNumMediaCartLabel(){
        return this.numMediaInCart;
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    @Override
    public void show() {
        numMediaInCart.setText(String.valueOf(Cart.getCart().getListMedia().size()) + " media");
        super.show();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new HomeController());
        try {
            List medium = getBController().getAllMedia();
            this.homeItems = new ArrayList();
            for (Object object : medium) {
                Media media = (Media) object;
                MediaForm m1 = new MediaForm(Configs.HOME_MEDIA_PATH, media, this);
                this.homeItems.add(m1);
            }
            this.filteredItems = new ArrayList<>(homeItems);
        } catch (SQLException | IOException e) {
            LOGGER.info("Errors occurred: " + e.getMessage());
            e.printStackTrace();
        }

        aimsImage.setOnMouseClicked(e -> addMediaHome(this.filteredItems));
        cartImage.setOnMouseClicked(e -> {
            try {
                LOGGER.info("User clicked to view cart");
                CartForm cartScreen = new CartForm(this.stage, Configs.CART_SCREEN_PATH);
                cartScreen.setHomeScreenHandler(this);
                cartScreen.setBController(new ViewCartController());
                cartScreen.requestToViewCart(this);
            } catch (IOException | SQLException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        addMediaHome(this.filteredItems);

        sortByPriceIncrease.setOnAction(e -> sortMediaByPrice(true));
        sortByPriceDecrease.setOnAction(e -> sortMediaByPrice(false));
        sortByAlphabetAZ.setOnAction(e -> sortMediaByTitle(true));
        sortByAlphabetZA.setOnAction(e -> sortMediaByTitle(false));

        filterBook.setOnAction(e -> filterMedia());
        filterDVD.setOnAction(e -> filterMedia());
        filterCD.setOnAction(e -> filterMedia());
    }

    public void setImage() {
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH_ICON + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);

        File file2 = new File(Configs.IMAGE_PATH_ICON + "/" + "cart.png");
        Image img2 = new Image(file2.toURI().toString());
        cartImage.setImage(img2);
    }

    @SuppressWarnings("rawtypes")
    public void addMediaHome(List items) {
        int size = items.size() / 4;
        System.out.println(size);
        ArrayList<MediaForm> mediaItems = new ArrayList<>((ArrayList<MediaForm>) items);
        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });
        while (!mediaItems.isEmpty()) {
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                while (vBox.getChildren().size() < size && !mediaItems.isEmpty()) {
                    MediaForm media = mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
            return;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addMenuItem(int position, String text, MenuButton menuButton){
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);
        menuItem.setOnAction(e -> {
            // empty home media
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            // filter only media with the choosen category
            List filteredItems = new ArrayList<>();
            homeItems.forEach(me -> {
                MediaForm media = (MediaForm) me;
                if (media.getMedia().getTitle().toLowerCase().startsWith(text.toLowerCase())){
                    filteredItems.add(media);
                }
            });

            // fill out the home with filted media as category
            addMediaHome(filteredItems);
        });
        menuButton.getItems().add(position, menuItem);
    }

    @FXML
    private void handleSearch() {
        LOGGER.info("Search button clicked");
        String keyword = searchTextField.getText().toLowerCase();
        try {
            List<Media> mediaList = new Media().getSearchMedia(keyword);
            List<MediaForm> mediaForms = new ArrayList<>();
            for (Media media : mediaList) {
                MediaForm mediaForm = new MediaForm(Configs.HOME_MEDIA_PATH, media, this);
                mediaForms.add(mediaForm);
            }
            this.homeItems = mediaForms;
            this.filteredItems = new ArrayList<>(homeItems);
            addMediaHome(mediaForms);
        } catch (SQLException | IOException e) {
            LOGGER.severe("Error fetching media data: " + e.getMessage());
        }
    }

    private void sortMediaByPrice(boolean ascending) {
        filteredItems.sort(Comparator.comparing((MediaForm m) -> m.getMedia().getPrice()));
        if (!ascending) {
            filteredItems.sort(Comparator.comparing((MediaForm m) -> m.getMedia().getPrice()).reversed());
        }
        addMediaHome(filteredItems);
    }

    private void sortMediaByTitle(boolean ascending) {
        filteredItems.sort(Comparator.comparing((MediaForm m) -> m.getMedia().getTitle()));
        if (!ascending) {
            filteredItems.sort(Comparator.comparing((MediaForm m) -> m.getMedia().getTitle()).reversed());
        }
        addMediaHome(filteredItems);
    }

    private void filterMedia() {
        filteredItems = new ArrayList<>();
        for (Object mediaForm : homeItems) {
            Media media = mediaForm instanceof MediaForm ? ((MediaForm) mediaForm).getMedia() : null;
            if ((filterBook.isSelected() && "Book".equalsIgnoreCase(media.getType())) ||
                    (filterDVD.isSelected() && "DVD".equalsIgnoreCase(media.getType())) ||
                    (filterCD.isSelected() && "CD".equalsIgnoreCase(media.getType()))) {
                filteredItems.add((MediaForm) mediaForm);
            }
        }
        addMediaHome(filteredItems);
    }
}