package org.example.personalizednewsrecommendationsystem.GUIContollers;

import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RecommendationsController {

    @FXML
    private ListView<Map<String, Object>> headlinesListView;  // ListView to display article recommendations

    private int userID;  // Stores the ID of the current user
    private String username;  // Stores the username of the current user


    // Setter method to set user ID
    public void setUserID(int userID) {
        this.userID = userID;
    }

    // Setter method to set username
    public void setUsername(String username) {
        this.username = username;
    }



    // Load recommendations when the controller is initialized
    @FXML
    private void initialize() {
        loadRecommendations();

        // Handle click events for items in the ListView
        headlinesListView.setOnMouseClicked(event -> {
            Map<String, Object> selectedArticle = headlinesListView.getSelectionModel().getSelectedItem();
            if (selectedArticle != null) {
                navigateToArticleDetails(selectedArticle);   // Navigate to article details view
            }
        });
    }


    // Load recommended articles based on user preferences
    private void loadRecommendations() {
        DatabaseManagement databaseManagement = new DatabaseManagement();

        // Ensure this block runs on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Get category scores for the user
            Map<String, Integer> categoryScores = databaseManagement.getCategoryScores(userID);

            // Check if there are no scores
            if (categoryScores == null || categoryScores.isEmpty()) {
                return;
            }

            // Get top 3 categories with the highest positive scores
            List<String> topCategories = categoryScores.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .map(Map.Entry::getKey)
                    .limit(3)
                    .toList();

            // Fetch articles for the top categories
            List<Map<String, Object>> recommendations = databaseManagement.getArticlesByCategories(topCategories);

            // Check if there are no recommendations
            if (recommendations == null || recommendations.isEmpty()) {
                return;
            }

            // Convert recommendations to observable list
            ObservableList<Map<String, Object>> observableArticles = FXCollections.observableArrayList(recommendations);
            headlinesListView.setItems(observableArticles);

            // Customize how items in the ListView are displayed
            headlinesListView.setCellFactory(listView -> new ListCell<>() {
                private final Label categoryLabel = new Label();
                private final Label headlineLabel = new Label();
                private final ImageView articleImageView = new ImageView();

                {   // Set styles for the category label
                    categoryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gray;");
                    headlineLabel.setWrapText(true);
                    headlineLabel.setMaxWidth(700);
                    articleImageView.setFitWidth(150);
                    articleImageView.setFitHeight(150);
                }

                @Override
                protected void updateItem(Map<String, Object> article, boolean empty) {
                    super.updateItem(article, empty);
                    if (empty || article == null) {
                        setGraphic(null);
                    } else {
                        String category = (String) article.get("Category");
                        String headline = (String) article.get("Headline");
                        String imagePath = (String) article.get("NewsImagePath");

                        categoryLabel.setText(category);
                        headlineLabel.setText(headline);

                        File imageFile = new File(imagePath);  // Load the article image or a placeholder if not found
                        if (imageFile.isFile()) {
                            articleImageView.setImage(new Image(imageFile.toURI().toString()));
                        } else {
                            articleImageView.setImage(new Image("path/to/placeholder/image.png"));
                        }

                        // Create a container for text elements
                        VBox textContainer = new VBox(categoryLabel, headlineLabel);
                        textContainer.setSpacing(5);

                        // Create a container for the entire cell content
                        HBox cellContainer = new HBox(articleImageView, textContainer);
                        cellContainer.setSpacing(10);

                        setGraphic(cellContainer);
                    }
                }
            });
        });
    }


    // Navigate to the article details view
    private void navigateToArticleDetails(Map<String, Object> article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/recommended-article-details.fxml"));
            Parent root = loader.load();

            RecommendedArticleDetailsController controller = loader.getController();

            // Pass user details and article data to the controller
            controller.setUserDetails(userID, username);
            controller.setArticleDetails(
                    (String) article.get("Description"),
                    (String) article.get("NewsImagePath")
            );

            Stage stage = (Stage) headlinesListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("NewsFlow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Navigate back to recommendation article menu
    public void onBackToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/main-dashboard.fxml"));
        Parent root = loader.load();

        MainDashboardController main = loader.getController();
        main.setUserID(userID);
        main.setUsername(username);
        main.setUserNameDisplay("Welcome " + username);

        Stage stage = (Stage) headlinesListView.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("NewsFlow");
    }
}
