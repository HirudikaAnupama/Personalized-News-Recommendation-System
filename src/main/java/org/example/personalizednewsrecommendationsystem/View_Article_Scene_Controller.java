package org.example.personalizednewsrecommendationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class View_Article_Scene_Controller {

    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView articleImageView;



    // Method to set the article description text
    public void setDescription(String description) {
        descriptionLabel.setText(description);
    }

    // Method to set the image based on the passed image path
    public void setImage(String imagePath) {
        if (imagePath != null) {
            Image image = new Image(imagePath);
            articleImageView.setImage(image);  // Set the image on the ImageView
        } else {
            // Optionally, set a default image if no image is found
            Image defaultImage = new Image(getClass().getResource("/path/to/default/image.png").toString());
            articleImageView.setImage(defaultImage);  // Set a default image
        }
    }
}
