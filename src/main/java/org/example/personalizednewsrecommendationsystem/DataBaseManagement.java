package org.example.personalizednewsrecommendationsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseManagement {
    private static final String URL = "jdbc:sqlite:NewsFlow_News_Recommendation_System_DataBase.db";

    public Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    public boolean checkUserExists(String username, String password) {
        String query = "SELECT COUNT(*) FROM UserTabel WHERE UserName = ? AND UserPassword = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking user: " + e.getMessage());
            return false;
        }
    }

    public void insertUser(String username, String password) {
        String query = "INSERT INTO UserTabel (UserName, UserPassword) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    public boolean checkArticleExists(String headline, String link) {
        String query = "SELECT COUNT(*) FROM Articles WHERE headline = ? OR links = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, headline);
            pstmt.setString(2, link);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking article existence: " + e.getMessage());
            return false;
        }
    }

    public void insertArticle(String headline, String shortDescription, String category) {
        String query = "INSERT INTO Articles (headline, description, category) VALUES (?, ?, ?)";
        int retryCount = 5;
        int delay = 1000;

        for (int i = 0; i < retryCount; i++) {
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, headline);
                pstmt.setString(2, shortDescription);
                pstmt.setString(3, category);

                pstmt.executeUpdate();
                System.out.println("Article inserted successfully.");
                break;
            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked")) {
                    System.out.println("Database is locked, retrying...");
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.out.println("Retry interrupted.");
                    }
                } else {
                    System.out.println("Error inserting article: " + e.getMessage());
                    break;
                }
            }
        }
    }


    public List<String> getHeadlinesWithCategories() {
        List<String> headlinesWithCategories = new ArrayList<>();
        String query = "SELECT category, headline FROM Articles";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String category = rs.getString("category");
                String headline = rs.getString("headline");
                headlinesWithCategories.add(category + "\n" + headline); // Format as "Category\nHeadline"
            }
        } catch (SQLException e) {
            System.out.println("Error fetching headlines with categories: " + e.getMessage());
        }
        return headlinesWithCategories;
    }




    public String getArticleDescription(String headline) {
        String query = "SELECT description FROM Articles WHERE headline = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, headline);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("description");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching description: " + e.getMessage());
        }
        return "Description not available.";
    }


    public int getUserID(String username) {
        String query = "SELECT UserID FROM UserTabel WHERE UserName = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching UserID: " + e.getMessage());
        }
        return -1; // Return -1 if UserID not found
    }

    // Retrieve ArticleID by headline
    public int getArticleID(String headline) {
        String query = "SELECT ArticleID FROM Articles WHERE headline = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, headline);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ArticleID");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching ArticleID: " + e.getMessage());
        }
        return -1; // Return -1 if ArticleID not found
    }

    // Store interaction in UserReadingHistory
    public void storeUserInteraction(int userID, int articleID, String interactionType) {
        String query = "INSERT INTO UserReadingHistory (UserID, ArticleID, InteractionType) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, articleID);
            pstmt.setString(3, interactionType);
            pstmt.executeUpdate();
            System.out.println("Interaction stored successfully.");
        } catch (SQLException e) {
            System.out.println("Error storing user interaction: " + e.getMessage());
        }
    }

    public void storeUserPreferences(int userId, String category, double preferenceScore) {
        String query = "INSERT INTO UserPreferences (UserID, Category, PreferenceScore) VALUES (?, ?, ?) " +
                "ON CONFLICT(UserID, Category) DO UPDATE SET PreferenceScore = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, category);
            pstmt.setDouble(3, preferenceScore);
            pstmt.setDouble(4, preferenceScore); // Update the score if already exists
            pstmt.executeUpdate();
            System.out.println("User preference stored/updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error storing user preference: " + e.getMessage());
        }
    }






    public List<String> getRecommendedArticlesByPreference(int userId, int limit) {
        List<String> recommendedArticles = new ArrayList<>();
        String query = """
    SELECT category, PreferenceScore FROM UserPreferences
    WHERE UserID = ? ORDER BY PreferenceScore DESC LIMIT ?;
    """;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String category = rs.getString("category");
                String fetchHeadlinesQuery = "SELECT headline FROM Articles WHERE category = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(fetchHeadlinesQuery)) {
                    pstmt2.setString(1, category);
                    ResultSet rs2 = pstmt2.executeQuery();
                    while (rs2.next()) {
                        recommendedArticles.add(rs2.getString("headline"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching recommended articles: " + e.getMessage());
        }
        return recommendedArticles;
    }

    // Example method for fetching recommended headlines (this is similar to the previous one)
    public List<String> getRecommendedHeadlinesByCategory(int userId, int categoryLimit) {
        List<String> recommendedHeadlines = getRecommendedArticlesByPreference(userId, categoryLimit);
        return recommendedHeadlines;
    }








}