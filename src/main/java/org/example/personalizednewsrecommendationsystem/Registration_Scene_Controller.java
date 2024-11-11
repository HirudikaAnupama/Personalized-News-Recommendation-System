package org.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Registration_Scene_Controller {

    @FXML
    private TextField registeredUserName;
    @FXML
    private TextField registeredUserPassword;
    @FXML
    private Label registrationMessage;
    private UserManagement userManagement;

    // Initialize method is called after the FXML file is loaded
    @FXML
    public void initialize() {
        DataBaseManagement dbHandler = new DataBaseManagement();
        userManagement = new UserManagement(dbHandler, this); // Pass 'this' to UserManagement
    }

    // Getter methods for TextField values
    public TextField getRegisteredUserName() {
        return registeredUserName;
    }

    public TextField getRegisteredUserPassword() {
        return registeredUserPassword;
    }

    // Event handler for the register button
    @FXML
    private void onRegisterButtonConfirm() {
        String registrationStatus = userManagement.registerUser(); // Register user
        registrationMessage.setText(registrationStatus);           // Display message
    }

    // Event handler for the back button
    public void onClickBackButtonInRegistration(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login-Scene.fxml")));
        stage.setScene(new Scene(parent, 720, 550));
        stage.setTitle("NewsFlow");
    }
}
