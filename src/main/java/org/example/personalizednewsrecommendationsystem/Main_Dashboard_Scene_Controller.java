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
    private Label userNameDisplay;


    public void OnViewArticles(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Article-List-Scene.fxml")));
        stage.setScene(new Scene(parent, 900, 650));
        stage.setTitle("NewsFlow");
    }

    public Label getUserNameDisplay() {
        return userNameDisplay;
    }

}
