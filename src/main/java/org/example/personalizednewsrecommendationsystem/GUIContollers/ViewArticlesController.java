package org.example.personalizednewsrecommendationsystem.GUIContollers;

import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendationsystem.RecommendationSystem.RecommendationEngine;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ViewArticlesController {
    public ListView<String> headlinesListView;  // ListView for displaying article details

    private int userID; // UserID passed from login scene
    private String username;  // username passed from login scene


    // Sets the user ID
    public void setUserID(int userID) {
        this.userID = userID;

    }
    // Sets the username
    public void setUsername(String  username){
        this.username = username;
    }



    // Initialization method called after FXML elements are loaded
    public void initialize() {
        DatabaseManagement databaseManagement= new DatabaseManagement();
        // Load articles from the database
        List<String> articles = databaseManagement.getHeadlinesWithCategories();
        ObservableList<String> observableArticles = FXCollections.observableArrayList(articles);
        headlinesListView.setItems(observableArticles);

        // Set custom cell factory for ListView to display headline, category, and image
        headlinesListView.setCellFactory(listView -> new ListCell<>() {
            private final Label categoryLabel = new Label(); // Displays article category
            private final Label headlineLabel = new Label(); // Displays article headline
            private final ImageView articleImageView = new ImageView(); // Displays article image

            {
                // Style configuration for labels and image view
                categoryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gray;");
                headlineLabel.setWrapText(true);
                headlineLabel.setMaxWidth(700); // Limit headline width
                articleImageView.setFitWidth(150); // Set image width
                articleImageView.setFitHeight(150); // Set image height
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null); // Clear graphic if the cell is empty
                } else {
                    // Parse article details from the input string
                    String[] parts = item.split("\n", 5);
                    String category = parts[1];
                    String headline = parts[2];
                    String imagePath = parts[4];

                    // Set category and headline text
                    categoryLabel.setText(category);
                    headlineLabel.setText(headline);

                    // Load and set article image if file exists
                    File imageFile = new File(imagePath);
                    if (imageFile.isFile()) {
                        articleImageView.setImage(new Image(imageFile.toURI().toString()));
                    } else {
                        articleImageView.setImage(null); // Placeholder for missing images
                    }

                    // Arrange category, headline, and image in a horizontal layout
                    VBox textContainer = new VBox(categoryLabel, headlineLabel);
                    textContainer.setSpacing(5);

                    HBox cellContainer = new HBox(articleImageView, textContainer);
                    cellContainer.setSpacing(10);

                    setGraphic(cellContainer);
                }
            }
        });

        // Add event listener for item selection in the ListView
        headlinesListView.setOnMouseClicked(event -> {
            String selectedArticle = headlinesListView.getSelectionModel().getSelectedItem();

            if (selectedArticle != null) {
                try {
                    showArticleDetails(selectedArticle); // Show detailed view for selected article
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    // Method to display details of the selected article
    private void showArticleDetails(String articleData) throws IOException {
        // Parse article data
        String[] parts = articleData.split("\n", 5);
        int articleID = Integer.parseInt(parts[0]); // Extract article ID
        String description = parts[3]; // Extract article description
        String imagePath = parts[4]; // Extract image path

        // Load the article details view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/article-details.fxml"));
        Parent root = loader.load();

        // Pass article details to the ArticleDetailsController
        ArticleDetailsController controller = loader.getController();
        controller.setArticleDetails(description, imagePath);
        controller.setUserAndArticleIDs(userID, username, articleID);

        // Switch to the article details scene
        Stage stage = (Stage) headlinesListView.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Article Details");
    }



    // Back to main menu
    public void onBackToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/main-dashboard.fxml"));
        Parent root = loader.load();

        MainDashboardController main = loader.getController();
        main.setUserID(userID); // Pass the userID back
        main.setUsername(username); // Pass the username back
        main.setUserNameDisplay("Welcome " + username); // Update display

        Stage stage = (Stage) headlinesListView.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Dashboard");
    }



    // Navigate to recommendation scene
    public void onRecommendations(ActionEvent event) throws IOException {
        // Load the FXML file using FXMLLoader
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/personalizednewsrecommendationsystem/recommendations.fxml")));
        Parent parent = loader.load();

        RecommendationsController recommendationsController = loader.getController();
        recommendationsController.setUserID(userID);
        recommendationsController.setUsername(username);

        // Pass the User ID  to the recommendation controller for generating recommendations.
        DatabaseManagement databaseManagement = new DatabaseManagement();
        RecommendationEngine recommendationEngine = new RecommendationEngine(databaseManagement);
        recommendationEngine.generateRecommendationsForUser(userID);

        // Switch to the new scene
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent, 900, 650));
        stage.setTitle("NewsFlow");
    }
}
