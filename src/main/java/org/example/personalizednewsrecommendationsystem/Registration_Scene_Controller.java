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

    private final UserManagement userManagement;

    public Registration_Scene_Controller() {
        DataBaseManagement dbHandler = new DataBaseManagement();
        this.userManagement = new UserManagement(dbHandler);
    }

    @FXML
    private void onRegisterButtonConfirm() {
        String username = registeredUserName.getText();
        String password = registeredUserPassword.getText();
        String registrationStatus = userManagement.registerUser(username, password);
        registrationMessage.setText(registrationStatus);
    }

    public void onClickBackButtonInRegistration(ActionEvent event) throws IOException {
        // Access the current stage from the button source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the Registration Scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login-Scene.fxml")));
        // Set the scene, dimensions, and title for the Registration window
        stage.setScene(new Scene(parent, 720, 550));
        stage.setTitle("NewsFlow");

    }
}
