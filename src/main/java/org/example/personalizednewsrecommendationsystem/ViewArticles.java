package org.example.personalizednewsrecommendationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewArticles {
    @FXML
    private Label descriptionLabel;

    // Method to set the article description in the label
    public void setDescription(String description) {
        descriptionLabel.setText(description);
    }
}
