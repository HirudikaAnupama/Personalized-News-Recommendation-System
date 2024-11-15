package org.example.personalizednewsrecommendationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.File;
import java.util.List;

public class Article_List_Scene_Controller {


    @FXML
    private ListView<String> headlinesListView;

    private final DataBaseManagement dbManager = new DataBaseManagement();

    @FXML
    public void initialize() {
        List<String> headlinesWithCategories = dbManager.getHeadlinesWithCategories();
        ObservableList<String> observableHeadlines = FXCollections.observableArrayList(headlinesWithCategories);
        headlinesListView.setItems(observableHeadlines);

        File imageDir = new File("src/main/resources/newsImage");
        File[] images = imageDir.listFiles();
        int imageCount = images != null ? images.length : 0;

        // Set custom cell factory to display category, headline, and image (if available)
        headlinesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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
                            String[] parts = item.split("\n", 2);
                            categoryLabel.setText(parts[0]);
                            headlineLabel.setText(parts[1]);

                            // Try to load the image based on index; if unavailable, use a placeholder or skip setting an image
                            try {
                                if (images != null && images.length > 0) {
                                    int imageIndex = getIndex() % imageCount;
                                    File imageFile = images[imageIndex];

                                    if (imageFile.isFile()) {
                                        Image image = new Image(imageFile.toURI().toString());
                                        imageView.setImage(image);
                                    } else {
                                        throw new Exception("File not found or not a valid file: " + imageFile.getName());
                                    }
                                } else {
                                    throw new Exception("Image directory is empty or not found");
                                }
                            } catch (Exception e) {
                                System.out.println("Error loading image: " + e.getMessage());
                                imageView.setImage(null); // Clear image or set a placeholder
                                // Optionally set a default placeholder image if available in resources
                                // Image placeholder = new Image(getClass().getResource("/path/to/placeholder.png").toString());
                                // imageView.setImage(placeholder);
                            }

                            // Arrange components in an HBox layout
                            VBox textBox = new VBox(categoryLabel, headlineLabel);
                            HBox hbox = new HBox(imageView, textBox);
                            hbox.setSpacing(10); // Add space between image and text
                            setGraphic(hbox);
                        }
                    }

                };
            }
        });

        // Add click listener to headlines
        headlinesListView.setOnMouseClicked(event -> {
            String selectedHeadline = headlinesListView.getSelectionModel().getSelectedItem();
            if (selectedHeadline != null) {
                String headline = selectedHeadline.split("\n", 2)[1];
                showDescriptionScene(headline);
            }
        });
    }

    public void showDescriptionScene(String headline) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View-Article-Scene.fxml"));
            Parent root = loader.load();

            // Get the description and image path for the selected headline
            String description = dbManager.getArticleDescription(headline);
            String imagePath = getImagePathForHeadline(headline);  // Get the image path relative to the headline

            // Pass the description and image path to the ViewArticles controller
            View_Article_Scene_Controller controller = loader.getController();
            controller.setDescription(description);
            controller.setImage(imagePath);  // Pass the image path

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Article Description");
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading ViewArticles scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getImagePathForHeadline(String headline) {
        // Get the index of the selected headline (for example, use index % imageCount to match images)
        int index = headlinesListView.getSelectionModel().getSelectedIndex();
        File imageDir = new File("src/main/resources/newsImage");
        File[] images = imageDir.listFiles();
        if (images != null && index < images.length) {
            return images[index].toURI().toString();  // Return the URI of the image corresponding to the headline
        }
        return null;  // Return null if no image found for the headline
    }



}
