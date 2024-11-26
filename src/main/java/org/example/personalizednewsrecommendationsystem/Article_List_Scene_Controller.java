package org.example.personalizednewsrecommendationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.List;

public class Article_List_Scene_Controller {

    @FXML
    private ListView<String> headlinesListView;

    private final DataBaseManagement dbManager = new DataBaseManagement();

    private int userID; // UserID passed from login scene

    public void setUserID(int userID) {
        this.userID = userID;
        // Additional logic based on userID
    }


    @FXML
    public void initialize() {
        // Load headlines with categories from the database
        List<String> headlinesWithCategories = dbManager.getHeadlinesWithCategories();
        ObservableList<String> observableHeadlines = FXCollections.observableArrayList(headlinesWithCategories);
        headlinesListView.setItems(observableHeadlines);

        // Directory for article images
        File imageDir = new File("src/main/resources/newsImage");
        File[] images = imageDir.listFiles();
        int imageCount = (images != null) ? images.length : 0;

        // Custom cell factory for ListView
        headlinesListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    private final Label categoryLabel = new Label();
                    private final Label headlineLabel = new Label();
                    private final ImageView imageView = new ImageView();

                    {
                        categoryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gray; -fx-font-size: 20px;");
                        headlineLabel.setWrapText(true);
                        headlineLabel.setMaxWidth(650);
                        imageView.setFitWidth(155);
                        imageView.setPreserveRatio(true);
                        setPrefWidth(0);
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            String[] parts = item.split("\n", 2); // Split category and headline
                            categoryLabel.setText(parts[0]);
                            headlineLabel.setText(parts[1]);

                            // Assign image based on index or default to placeholder
                            try {
                                if (images != null && imageCount > 0) {
                                    int imageIndex = getIndex() % imageCount;
                                    File imageFile = images[imageIndex];

                                    if (imageFile.isFile()) {
                                        Image image = new Image(imageFile.toURI().toString());
                                        imageView.setImage(image);
                                    } else {
                                        throw new Exception("Invalid image file: " + imageFile.getName());
                                    }
                                } else {
                                    throw new Exception("No images available in the directory");
                                }
                            } catch (Exception e) {
                                System.out.println("Image loading error: " + e.getMessage());
                                imageView.setImage(null); // Optional: Set a placeholder image
                            }

                            // Arrange content in a layout
                            VBox textBox = new VBox(categoryLabel, headlineLabel);
                            HBox hbox = new HBox(imageView, textBox);
                            hbox.setSpacing(10);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        // Handle clicks on headlines
        headlinesListView.setOnMouseClicked(event -> {
            String selectedHeadline = headlinesListView.getSelectionModel().getSelectedItem();
            if (selectedHeadline != null) {
                String headline = selectedHeadline.split("\n", 2)[1];
                int articleID = dbManager.getArticleID(headline); // Retrieve ArticleID
                showDescriptionScene(headline, articleID);


            }
        });
    }

    private void showDescriptionScene(String headline, int articleID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View-Article-Scene.fxml"));
            Parent root = loader.load();

            // Get article description and image path
            String description = dbManager.getArticleDescription(headline);
            String imagePath = getImagePathForHeadline(articleID);

            // Pass data to the View-Article controller
            View_Article_Scene_Controller controller = loader.getController();
            controller.setUserAndArticle(userID, articleID);
            controller.setDescription(description);
            controller.setImage(imagePath);

            // Get the current stage and set the new scene
            Stage stage = (Stage) headlinesListView.getScene().getWindow(); // Replace userNameDisplay with any existing node
            stage.setScene(new Scene(root));
            stage.setTitle("Article Details");
        } catch (Exception e) {
            System.out.println("Error loading View-Article scene: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private String getImagePathForHeadline(int articleID) {
        File imageDir = new File("src/main/resources/newsImage");
        File[] images = imageDir.listFiles();
        if (images != null && articleID % images.length < images.length) {
            return images[articleID % images.length].toURI().toString(); // Return corresponding image URI
        }
        return null; // No image available
    }
}
