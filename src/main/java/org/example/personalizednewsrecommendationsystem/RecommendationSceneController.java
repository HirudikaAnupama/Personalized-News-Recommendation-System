package org.example.personalizednewsrecommendationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class RecommendationSceneController {

    @FXML
    public ListView<String> recommendationListView;

    private final DataBaseManagement dbManager = new DataBaseManagement();

    /**
     * Method to load recommendations for a user based on their preferences.
     * @param userId The ID of the user for whom recommendations are to be fetched.
     */
    public void loadRecommendations(int userId) {
        // Fetch the recommended headlines based on the user's preferences
        List<String> recommendedHeadlines = dbManager.getRecommendedHeadlinesByCategory(userId, 5); // Limit to 5 categories

        // Update the UI with the fetched recommendations or a "No recommendations" message
        if (recommendedHeadlines.isEmpty()) {
            recommendationListView.getItems().setAll("No recommendations available.");
        } else {
            recommendationListView.getItems().setAll(recommendedHeadlines);
        }

        // Optionally: You can store or update user preferences if required
        // Assuming you have logic to determine preferenceScore and category for storing user preferences
        double preferenceScore = 0.8; // Example preference score, you can calculate it based on user interactions
        String category = "Technology"; // Example category, it can vary
        dbManager.storeUserPreferences(userId, category, preferenceScore);
    }

    /**
     * Example method to demonstrate how to call loadRecommendations.
     * This can be invoked when the scene is initialized or on a button click.
     * @param userId The ID of the currently logged-in user.
     */
    public void initialize(int userId) {
        loadRecommendations(userId); // Call the method to load recommendations
    }
}
