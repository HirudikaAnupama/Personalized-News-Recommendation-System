package org.example.personalizednewsrecommendationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;

public class Main_Dashboard_Scene_Controller {

    @FXML
    private ListView<String> headlinesListView;

    private final DataBaseManagement dbManager = new DataBaseManagement();

    @FXML
    public void initialize() {
        List<String> headlines = dbManager.getHeadlines();
        ObservableList<String> observableHeadlines = FXCollections.observableArrayList(headlines);
        headlinesListView.setItems(observableHeadlines);

        // Set custom cell factory to wrap text in each cell
        headlinesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    private final Label label = new Label();

                    {
                        label.setWrapText(true); // Enable text wrapping
                        label.setMaxWidth(800); // Set maximum width for the text
                        setPrefWidth(0); // Allows the label to use the full width of the ListView
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            label.setText(item);
                            setGraphic(label);
                        }
                    }
                };
            }
        });

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalizednewsrecommendationsystem/View_Articles.fxml"));
            Parent root = loader.load();

            // Retrieve the article description from the database
            String description = dbManager.getArticleDescription(headline);

            // Pass the description to the ViewArticles controller
            ViewArticles controller = loader.getController();
            controller.setDescription(description);

            // Display the new stage with the article description
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Article Description");
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading ViewArticles scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
