package org.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class View_Article_Scene_Controller {

    @FXML
    public Button liked;
    @FXML
    public Button skipped;
    @FXML
    public Button read;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView articleImageView;

    private final DataBaseManagement dbManager = new DataBaseManagement();
    private int userID;
    private int articleID;

    // Set UserID and ArticleID (called when scene is loaded)
    public void setUserAndArticle(int userID, int articleID) {
        this.userID = userID;
        this.articleID = articleID;
    }

    // Method to set the article description text
    public void setDescription(String description) {
        descriptionLabel.setText(description);
    }

    // Method to set the image based on the passed image path
    public void setImage(String imagePath) {
        if (imagePath != null) {
            Image image = new Image(imagePath);
            articleImageView.setImage(image); // Set the image on the ImageView
        } else {
            // Optionally, set a default image if no image is found
            Image defaultImage = new Image(getClass().getResource("/path/to/default/image.png").toString());
            articleImageView.setImage(defaultImage); // Set a default image
        }
    }

    public void onBackToViewArticleList(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Article-List-Scene.fxml")));
        stage.setScene(new Scene(parent, 720, 550));
        stage.setTitle("NewsFlow");
    }

    // Store interaction type "Liked"
    public void onLikedButtonClick(ActionEvent event) {
        dbManager.storeUserInteraction(userID, articleID, "Liked");
    }

    // Store interaction type "Skipped"
    public void onSkippedButtonClick(ActionEvent event) {
        dbManager.storeUserInteraction(userID, articleID, "Skipped");
    }

    // Store interaction type "Read"
    public void onReadButtonClick(ActionEvent event) {
        dbManager.storeUserInteraction(userID, articleID, "Read");

    }


}
