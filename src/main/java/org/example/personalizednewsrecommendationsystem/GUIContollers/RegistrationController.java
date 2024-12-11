package org.example.personalizednewsrecommendationsystem.GUIContollers;

import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;
import org.example.personalizednewsrecommendationsystem.UserManagement.NewUserManagement;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
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

public class RegistrationController {
    @FXML
    public TextField registeredUserName; // TextField for the username
    @FXML
    public PasswordField registeredUserPassword; // PasswordField for the password
    @FXML
    public Label registrationMessage; // Label to display registration messages


    private LoginController loginController; // Login scene controller


    // Method for set logging scene controller
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }


    // Gets the username from the TextField
    public String getRegisteredUserName() {
        return registeredUserName.getText().trim();
    }


    // Gets the password from the PasswordField
    public String getRegisteredUserPassword() {
        return registeredUserPassword.getText();
    }


    // Sets a registration message
    public void setRegistrationMessage(String message) {
        registrationMessage.setText(message);
    }




    // Handles the registration button click event and connect with "Registration"
    public void onRegisterButtonConfirm(ActionEvent event) {
        NewUserManagement userManagement = new NewUserManagement(
                this,
                loginController,
                new DatabaseManagement(),
                new ConcurrencyManagement(10));
        userManagement.register();
    }



   // Handles the back button click event in the registration screen
    public void onClickBackButtonInRegistration(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/personalizednewsrecommendationsystem/login.fxml")));
        stage.setScene(new Scene(parent, 720, 550));
        stage.setTitle("NewsFlow");
    }
}
