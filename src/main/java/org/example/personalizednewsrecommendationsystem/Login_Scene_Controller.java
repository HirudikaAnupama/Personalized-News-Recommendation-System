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
    private final UserManagement userManagement;


    @FXML
    private TextField loggingUserName;
    @FXML
    private TextField loggingUserPassword;
    @FXML
    private Label loggingMessage;

    ArticleManagement a = new ArticleManagement();




    public Login_Scene_Controller() {
        Registration_Scene_Controller register = new Registration_Scene_Controller();
        DataBaseManagement dbHandler = new DataBaseManagement();
        this.userManagement = new UserManagement(dbHandler, register);
    }

    @FXML
    private void onRegisterButtonClick(ActionEvent event) throws IOException {
        navigateToScene(event, "Registration-Scene.fxml");


    }



    @FXML
    public void onLoginSuccess(ActionEvent event) throws IOException {
        String username = loggingUserName.getText();
        String password = loggingUserPassword.getText();

        if (userManagement.authenticateUser(username, password)) {
            // Load the FXML file and retrieve the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main-Dashboard-Scene.fxml"));
            Parent parent = loader.load();

            // Retrieve the controller from the loader
            Main_Dashboard_Scene_Controller mainController = loader.getController();

            // Set the welcome message with the username
            mainController.getUserNameDisplay().setText("Welcome " + username);

            // Show the Main Dashboard Scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent, 720, 550));
        } else {
            loggingMessage.setText("Invalid credentials *");
        }
    }




    private void navigateToScene(ActionEvent event, String fxmlFile) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        stage.setScene(new Scene(parent, 720, 550));
    }
}
