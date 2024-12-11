package org.example.personalizednewsrecommendationsystem.GUIContollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;
import org.example.personalizednewsrecommendationsystem.RecommendationSystem.RecommendationEngine;

import java.io.IOException;
import java.util.Objects;

public class MainDashboardController {

    @FXML
    public Label userNameDisplay;  // Username welcome message label

    private int userID;    // Track login user id for main menu
    private String username;  // Track login username for main menu



    // Method for set userID and call it when login
    public void setUserID(int userID) {
        this.userID = userID;
    }


    // Method for set username and call it when login
    public void setUsername(String username){
        this.username = username;
    }


    // Method for set name in display label and call it when login
    public void setUserNameDisplay(String name) {
        this.userNameDisplay.setText(name);
    }




    // Recommendation scene navigation button method
    public void onGetRecommendations(ActionEvent event) throws IOException {

        // Load the FXML file using FXMLLoader
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/personalizednewsrecommendationsystem/recommendations.fxml")));
        Parent parent = loader.load();

        // Send username and user ID to provide recommendations
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




    // Browse articles menu navigation button method
    public void onViewArticles(ActionEvent event) throws IOException {
        // Load the FXML file using FXMLLoader
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/personalizednewsrecommendationsystem/view-articles.fxml")));
        Parent parent = loader.load();

        // Get the controller from the FXMLLoader
        ViewArticlesController viewArticlesController = loader.getController();

        // Pass the userID to the controller
        viewArticlesController.setUserID(userID);
        viewArticlesController.setUsername(username);

        // Switch to the new scene
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent, 900, 650));
        stage.setTitle("NewsFlow");

    }




    // Logout scene if user want logout
    public void onLogout(MouseEvent mouseEvent) throws IOException {
        ImageView imageView = (ImageView) mouseEvent.getSource();  // Cast to ImageView
        Stage stage = (Stage) imageView.getScene().getWindow();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/personalizednewsrecommendationsystem/login.fxml")));
        stage.setScene(new Scene(parent, 720, 550));
        stage.setTitle("NewsFlow");
    }


}
