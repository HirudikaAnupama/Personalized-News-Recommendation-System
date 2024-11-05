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

public class Login_Scene_Controller {

    DataBaseManagement DB = new DataBaseManagement();


    public Label loggingMessage;
    @FXML
    private TextField loggingUserName;
    @FXML
    private TextField loggingUserPassword;


    public TextField getLoggingUserPassword() {
        return loggingUserPassword;
    }

    public TextField getLoggingUserName() {
        return loggingUserName;
    }

    public Label getLoggingMessage() {
        return loggingMessage;
    }

    @FXML
    private void onRegisterButtonClick(ActionEvent event) throws IOException {
        // Access the current stage from the button source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the Registration Scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Registration-Scene.fxml")));
        // Set the scene, dimensions, and title for the Registration window
        stage.setScene(new Scene(parent, 700, 500));
        stage.setTitle("NewsFlow");
    }


    public void onLoginSuccess(ActionEvent event) throws IOException {
        UserManagement u1 = new UserManagement(this); // Pass the current controller instance
        // Get the username and password
        String name = loggingUserName.getText();
        String password = loggingUserPassword.getText();

        // Check if either field is empty
        if (name.isEmpty() || password.isEmpty()) {
            loggingMessage.setText("Username or Password cannot be empty");
            return; // Exit the method early if any field is empty
        }

        // If fields are not empty, proceed with the check
        u1.check();

        if (!DB.checkUserExists(name,password)){
            return;
        }

        // Access the current stage from the button source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the Main Dashboard Scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main-Dashboard-Scene.fxml")));
        // Set the scene, dimensions, and title for the Main Dashboard window
        stage.setScene(new Scene(parent, 700, 500));
        stage.setTitle("NewsFlow");
    }







}
