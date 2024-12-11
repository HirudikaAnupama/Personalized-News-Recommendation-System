package org.example.personalizednewsrecommendationsystem.GUIContollers;

import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;
import org.example.personalizednewsrecommendationsystem.RecommendationSystem.RecommendationEngine;
import org.example.personalizednewsrecommendationsystem.UserManagement.UserManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField loggingUserName;  // TextField for the username

    @FXML
    private PasswordField loggingUserPassword;  // PasswordField for the password

    @FXML
    private Label loggingMessage;  // Label to display registration messages


    // Gets the username from the TextField
    public String getLoggingUserName() {
        return loggingUserName.getText();
    }


    // Gets the password from the PasswordField
    public String getLoggingUserPassword() {
        return loggingUserPassword.getText();
    }


    // Sets a logging message
    public Label getLoggingMessage() {
        return loggingMessage;
    }



    // If all criteria for user=login are successful, this function navigates user to the main-menu
    // Connect with "Login" and "Main menu"
    @FXML
    public void onLoginSuccess(ActionEvent event) throws IOException {
        // Initialized relevant class objects for associated activities
        DatabaseManagement databaseManagement = new DatabaseManagement();
        ConcurrencyManagement concurrencyManagement = new ConcurrencyManagement(10);
        UserManagement userManagement = new UserManagement(this, databaseManagement, concurrencyManagement);

        // Check if the login is successful by invoking the login method of UserManagement.
        if (userManagement.login()) {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/personalizednewsrecommendationsystem/main-dashboard.fxml")));
            Parent parent = loader.load();

            // Display username in label and call main-menu controller class
            MainDashboardController main = loader.getController();
            main.setUserNameDisplay("Welcome " + userManagement.getUsername());

            // Retrieve the user ID from the database to identify the user ID when the user is logged in.
            int userID = databaseManagement.getUserID(loggingUserName.getText());

            // Pass the User ID and username to the main dashboard controller.
            main.setUserID(userID);
            main.setUsername(loggingUserName.getText());



            // Set the scene and show the stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent, 720, 550));
            stage.setTitle("NewsFlow");
        }
    }



    // Navigates to registration scene from login scene
    @FXML
    public void onRegisterButtonClick(ActionEvent event) throws IOException {
        // Load the FXML file and initialize the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/register.fxml"));
        Parent parent = loader.load();

        // Get the RegistrationController and set the LoginController
        RegistrationController registrationController = loader.getController();
        registrationController.setLoginController(this); // Pass the current LoginController instance

        // Set the scene and show the stage
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent, 720, 550));
        stage.setTitle("NewsFlow");
    }

}
