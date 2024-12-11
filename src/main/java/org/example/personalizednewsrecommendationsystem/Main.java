package org.example.personalizednewsrecommendationsystem;

import org.example.personalizednewsrecommendationsystem.ArticleManagement.ArticleFetcher;
import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    private ConcurrencyManagement concurrencyManagement;

    @Override
    public void start(Stage stage) throws IOException {
        // UI structure
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Image icon = new Image("Logo.jpg");
        stage.getIcons().add(icon);
        Scene scene = new Scene(fxmlLoader.load(), 720, 550);
        stage.setTitle("NewsFlow");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();


        // Create ConcurrencyManagement instance for background tasks
        this.concurrencyManagement = new ConcurrencyManagement(10);


        // Initialize concurrency in database when execute system
        DatabaseManagement databaseManagement = new DatabaseManagement();
        databaseManagement.handleConcurrentConnection();

        // Initialize article fetcher when execute system
        ArticleFetcher articleFetcher = new ArticleFetcher();
        articleFetcher.loadArticlesFromCSV();



    }

    @Override
    public void stop() {
        // Ensure ConcurrencyManagement is properly shut down
        if (concurrencyManagement != null) {
            concurrencyManagement.shutdown();
        }

        // Add other cleanup logic if necessary
        System.out.println("Application stopped. Resources released.");
    }

    public static void main(String[] args) {
        launch();
    }
}
