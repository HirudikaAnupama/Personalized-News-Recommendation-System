package org.example.personalizednewsrecommendationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.List;

public class Main_Dashboard_Scene_Controller {

    @FXML
    private ListView<String> headlinesListView;

    private DataBaseManagement dbManager = new DataBaseManagement();

    @FXML
    public void initialize() {
        List<String> headlines = dbManager.getHeadlines();
        ObservableList<String> observableHeadlines = FXCollections.observableArrayList(headlines);
        headlinesListView.setItems(observableHeadlines);

        // Add click listener to headlines
        headlinesListView.setOnMouseClicked(event -> {
            String selectedHeadline = headlinesListView.getSelectionModel().getSelectedItem();
            if (selectedHeadline != null) {
                showDescriptionScene(selectedHeadline);
            }
        });
    }

    public void showDescriptionScene(String headline) {
        try {
            // Correct the resource path: start from the root of resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/View_Articles.fxml"));

            if (loader.getLocation() == null) {
                System.out.println("Error: FXML file location not found.");
                return;
            }

            Parent root = loader.load();

            // Retrieve the article description from the database
            String description = dbManager.getArticleDescription(headline);

            // Set the description in the ViewArticles controller
            ViewArticles controller = loader.getController();
            controller.setDescription(description);

            // Display the new stage with the article description
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Article Description");
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading ViewArticles scene: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for more details
        }
    }


}
