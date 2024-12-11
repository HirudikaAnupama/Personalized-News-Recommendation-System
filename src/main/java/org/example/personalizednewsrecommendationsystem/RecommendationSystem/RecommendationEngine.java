package org.example.personalizednewsrecommendationsystem.RecommendationSystem;

import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;

import java.util.*;
import java.util.stream.Collectors;


public class RecommendationEngine {

    private final DatabaseManagement databaseManagement;


    // Constructor to initialize the database management object
    public RecommendationEngine(DatabaseManagement databaseManagement) {
        this.databaseManagement = databaseManagement;
    }


    // Generates recommendations for a single user.
    public void generateRecommendationsForUser(int userID) {
        ConcurrencyManagement concurrencyManagement = new ConcurrencyManagement(10);
        concurrencyManagement.handleTask(() -> {
            // Fetch interactions for the user.
            List<Map<String, Object>> userInteractions = databaseManagement.getUserInteractionsByUserID(userID);

            // Check if user has no interactions.
            if (userInteractions == null || userInteractions.isEmpty()) {
                return;
            }

            // Calculate category scores based on interactions.
            Map<String, Double> categoryScores = calculateCategoryScores(userInteractions);

            // Normalize the category scores.
            normalizeCategoryScores(categoryScores);

            // Sort the categories by their scores.
            List<Map.Entry<String, Double>> sortedRecommendations = categoryScores.entrySet().stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // Sort by score descending.
                    .collect(Collectors.toList());

            // Store the recommendations for the user.
            storeRecommendations(userID, sortedRecommendations);

        });
    }



    // Calculates the category scores based on user interactions.
    private Map<String, Double> calculateCategoryScores(List<Map<String, Object>> interactions) {
        Map<String, Double> categoryScores = new HashMap<>();

        // Process each user interaction
        for (Map<String, Object> interaction : interactions) {
            int articleID = (int) interaction.get("ArticleID"); // Get article ID
            String interactionType = (String) interaction.get("InteractionType"); // Get interaction type
            String category = databaseManagement.getCategoryByArticleID(articleID); // Get article category

            // Calculate score if category exists
            if (category != null) {
                double score = calculateInteractionScore(interactionType); // Get interaction score
                categoryScores.merge(category, score, Double::sum); // Add score to category
            }
        }
        return categoryScores;
    }



    // Normalizes the category scores to ensure all values are between 0 and 1.
    private void normalizeCategoryScores(Map<String, Double> categoryScores) {
        double totalScore = categoryScores.values().stream().mapToDouble(Double::doubleValue).sum(); // Get total score

        // Normalize scores if total is positive
        if (totalScore > 0) {
            categoryScores.replaceAll((category, score) -> score / totalScore);
        } else {
            // Assign equal scores if total is zero
            categoryScores.replaceAll((category, score) -> 1.0 / categoryScores.size());
        }
    }



    // Returns a score based on the type of user interaction.
    private double calculateInteractionScore(String interactionType) {

        return switch (interactionType.toLowerCase()) {
            case "liked" -> 10.0; // Liked gives high score
            case "read" -> 5.0; // Read gives medium score
            case "skipped" -> -2.0; // Skipped gives negative score
            default -> 0.0; // Unknown interaction type gives zero score
        };
    }



    // Save recommendations to the database
    private void storeRecommendations(int userID, List<Map.Entry<String, Double>> sortedCategoryScores) {
        databaseManagement.storeRecommendations(userID, sortedCategoryScores);
    }
}
