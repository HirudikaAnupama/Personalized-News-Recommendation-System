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

    @FXML
    private TextField loggingUserName;
    @FXML
    private TextField loggingUserPassword;




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
        // Access the current stage from the button source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the Registration Scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main-Dashboard-Scene.fxml")));
        // Set the scene, dimensions, and title for the Registration window
        stage.setScene(new Scene(parent, 700, 500));
        stage.setTitle("NewsFlow");

    }


}
