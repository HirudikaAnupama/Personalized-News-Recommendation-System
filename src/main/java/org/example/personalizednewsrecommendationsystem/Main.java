package org.example.personalizednewsrecommendationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login-Scene.fxml"));

        Image icon = new Image("Logo.jpg");
        stage.getIcons().add(icon);


        Scene scene = new Scene(fxmlLoader.load(), 720, 550);
        stage.setTitle("NewsFlow");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}