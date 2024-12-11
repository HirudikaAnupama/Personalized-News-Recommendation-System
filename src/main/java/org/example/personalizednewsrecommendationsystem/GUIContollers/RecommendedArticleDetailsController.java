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

import java.io.File;
import java.io.IOException;

public class RecommendedArticleDetailsController {

    // UI elements from FXML
    public Label descriptionLabel;
    public ImageView articleImageView;


    // Variables to store user data
    private int userID;
    private String username;


    // Setter methods for user data
    public void setUserDetails(int userID, String username) {
        this.userID = userID;
        this.username = username;
    }



    // Method to display article details including description and image
    public void setArticleDetails(String description, String imagePath) {
        descriptionLabel.setText(description != null ? description : "No description available.");
        // Set the article image if valid
        File imageFile = new File(imagePath);
        if (imageFile.isFile()) {
            articleImageView.setImage(new Image(imageFile.toURI().toString()));
        } else {
            articleImageView.setImage(new Image("path/to/placeholder/image.png")); // Add placeholder image
        }
    }



    // Method to navigate back to the recommendation list
    public void onBackToViewArticleList(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow(); // Get current stage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/recommendations.fxml"));
            Parent root = loader.load(); // Load the article list view

            // Pass the userID and username back to ViewArticlesController
            RecommendationsController controller = loader.getController();
            controller.setUserID(userID); // Pass the userID back
            controller.setUsername(username); // Pass the username back

            stage.setScene(new Scene(root)); // Set new scene with the loaded view
            stage.setTitle("NewsFlow"); // Set the title of the stage
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if view loading fails
        }
    }
}
