package org.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main_Dashboard_Scene_Controller {

    @FXML
    private Label userNameDisplay; // Label to display user's name or a welcome message

    private int userID; // The ID of the logged-in user
    private String userName; // The name of the logged-in user


    public void setUserID(int userID) {
        this.userID = userID;
    }


    public void setUserName(String userName) {
        this.userName = userName;
        updateUserDisplay();
    }

    /**
     * Updates the user display with the logged-in username.
     */
    private void updateUserDisplay() {
        if (userNameDisplay != null) {
            userNameDisplay.setText("Welcome " + userName);
        }
    }

    @FXML
    public void OnViewArticles(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Article-List-Scene.fxml"));
            Parent parent = loader.load();

            // Pass the userID to the Article List Scene Controller if needed
            Article_List_Scene_Controller articleController = loader.getController();
            articleController.setUserID(userID);

            stage.setScene(new Scene(parent, 900, 650));
            stage.setTitle("NewsFlow");
        } catch (IOException e) {
            System.err.println("Error loading Article-List-Scene.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onRecommendation(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Recommendation-Scene.fxml"));
        Parent parent = loader.load();

        // Get the controller and pass userId to load recommendations
        RecommendationSceneController recommendationController = loader.getController();
        recommendationController.initialize(userID);  // Pass the logged-in user's ID

        stage.setScene(new Scene(parent, 950, 650));
        stage.setTitle("NewsFlow");
    }


}
