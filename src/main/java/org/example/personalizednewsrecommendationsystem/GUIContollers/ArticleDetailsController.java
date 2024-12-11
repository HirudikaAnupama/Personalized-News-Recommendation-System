package org.example.personalizednewsrecommendationsystem.GUIContollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;

import java.io.File;
import java.io.IOException;

public class ArticleDetailsController {
    // UI elements from FXML
    public ImageView articleImageView;
    public Label descriptionLabel;
    public Label RatingDisplay;
    public Button liked;
    public Button skipped;
    public Button read;


    // Variables to store user and article data
    private int userID;
    private int articleID;
    private String username;

    // Database manager instance
    private final DatabaseManagement databaseManagement = new DatabaseManagement();


    // Method to set user and article IDs
    public void setUserAndArticleIDs(int userID, String username, int articleID) {
        this.userID = userID;
        this.username = username;
        this.articleID = articleID;
    }



    // Method to display article details including description and image
    public void setArticleDetails(String description, String imagePath) {
        descriptionLabel.setText(description); // Set the article description text
        // Set the article image if valid
        File imageFile = new File(imagePath);
        if (imageFile.isFile()) {
            articleImageView.setImage(new Image(imageFile.toURI().toString()));
        } else {
            articleImageView.setImage(null); // Placeholder for missing image
        }
    }



    // Method to store user liked articles with user ID and article ID
    public void onLikedButtonClick(ActionEvent event) {
        databaseManagement.storeUserInteraction(userID, articleID, "Liked");
        RatingDisplay.setText("Added a like to the article");
    }

    /// Method to store user skipped articles with user ID and article ID
    public void onSkippedButtonClick(ActionEvent event) {
        databaseManagement.storeUserInteraction(userID, articleID, "Skipped");
        RatingDisplay.setText("Skipped article!");

    }

    // Method to store user read articles with user ID and article ID
    public void onReadButtonClick(ActionEvent event) {
        databaseManagement.storeUserInteraction(userID, articleID, "Read");
        RatingDisplay.setText("Confirmed as read the article");
    }



    // Method to navigate back to the article list view
    public void onBackToViewArticleList(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); // Get current stage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/view-articles.fxml"));
            Parent root = loader.load(); // Load the article list view

            // Pass the userID and username back to ViewArticlesController
            ViewArticlesController controller = loader.getController();
            controller.setUserID(userID); // Pass the userID back
            controller.setUsername(username); // Pass the username back

            stage.setScene(new Scene(root)); // Set new scene with the loaded view
            stage.setTitle("NewsFlow"); // Set the title of the stage
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if view loading fails
        }
    }
}
