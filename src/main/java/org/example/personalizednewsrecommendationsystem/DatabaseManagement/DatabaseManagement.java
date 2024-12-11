package org.example.personalizednewsrecommendationsystem.DatabaseManagement;

import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseManagement {


    //Database path
    private static final String DATABASE_URL = "jdbc:sqlite:Personalized News Recommendation System Database.db";


    // Handles concurrent connections by submitting connection tasks to a thread pool.
    public void handleConcurrentConnection() {
        ConcurrencyManagement concurrencyManagement = new ConcurrencyManagement(10);
        concurrencyManagement.handleTask(() -> {
            Connection connection = null;
            try {
                connection = connect();
                if (connection != null) {
                    System.out.println("Connection successful!");
                    // Perform database operations here if needed
                } else {
                    System.out.println("Connection failed.");
                }
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing connection: " + e.getMessage());
                    }
                }
            }
        });
    }


    // Establishes a database connection.
    private Connection connect() {
        try {
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }




    // Check user details already exists
    public boolean checkUserExists(String username, String password) {
        String query = "SELECT COUNT(*) FROM UserTable WHERE UserName = ? AND UserPassword = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking user: " + e.getMessage());
            return false;
        }
    }



    // Check same username exists or not
    public boolean checkUserName(String username){
        String query = "SELECT COUNT(*) FROM UserTable WHERE UserName = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking userName: " + e.getMessage());
            return false;
        }

    }


    // Insert user data into UserTable
    public void storeUserDetails(String userName, String password) {
        String query = "INSERT INTO UserTable (UserName, UserPassword) VALUES (?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }




    // Get user ID by username
    public int getUserID(String username) {
        String query = "SELECT UserID FROM UserTable WHERE UserName = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching UserID: " + e.getMessage());
        }
        return -1; // Return -1 if UserID not found
    }




    // Check if insert article is already inside the database
    public boolean checkArticleExists(String headline, String description) {
        String query = "SELECT COUNT(*) FROM ArticleTable WHERE Headline = ? AND Description = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, headline);
            statement.setString(2, description);
            ResultSet rs = statement.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking article existence: " + e.getMessage());
            return false;
        }
    }
    // Insert articles into database
    public void insertArticle(String headline, String description, String category, String imagePath) {
        // Check if the article already exists
        if (checkArticleExists(headline, description)) {
            return;
        }
        String query = "INSERT INTO ArticleTable (Headline, Description, Category, NewsImagePath) VALUES (?, ?, ?, ?)";
        int retryCount = 5;
        int delay = 1000; // Delay in milliseconds between retries

        for (int i = 0; i < retryCount; i++) {
            try (Connection connection = connect();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, headline);
                statement.setString(2, description);
                statement.setString(3, category);
                statement.setString(4, imagePath);
                statement.executeUpdate();
                break; // Exit loop after successful insertion
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
                    break; // Exit loop on other SQL errors
                }
            }
        }
    }




    // Return headline and category
    public List<String> getHeadlinesWithCategories() {
        List<String> headlinesWithCategories = new ArrayList<>();
        String query = "SELECT ArticleID, Category, Headline, Description, NewsImagePath FROM ArticleTable";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                // Concatenate necessary data, including ArticleID and Description
                String articleID = rs.getString("ArticleID");
                String category = rs.getString("Category");
                String headline = rs.getString("Headline");
                String description = rs.getString("Description");
                String imagePath = rs.getString("NewsImagePath");

                // Store data in a structured way
                headlinesWithCategories.add(
                        articleID + "\n" + category + "\n" + headline + "\n" + description + "\n" + imagePath
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching headlines with categories: " + e.getMessage());
        }
        return headlinesWithCategories;
    }





    // Store user interactions in to "ReadingHistoryTable" table
    public void storeUserInteraction(int userID, int articleID, String interactionType) {
        String query = "INSERT INTO ReadingHistoryTable (UserID, ArticleID, InterctionType) VALUES (?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            statement.setInt(2, articleID);
            statement.setString(3, interactionType);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving interaction: " + e.getMessage());
        }
    }




    // Return user preferences
    public List<Map<String, Object>> getUserInteractionsByUserID(int userID) {
        // Correct column name from the schema: "InterctionType"
        String query = "SELECT UserID, ArticleID, InterctionType FROM ReadingHistoryTable WHERE UserID = ?";
        List<Map<String, Object>> interactions = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the userID parameter in the query
            statement.setInt(1, userID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> interaction = new HashMap<>();
                    interaction.put("UserID", resultSet.getInt("UserID"));
                    interaction.put("ArticleID", resultSet.getInt("ArticleID"));
                    interaction.put("InteractionType", resultSet.getString("InterctionType"));
                    interactions.add(interaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching interactions for user " + userID + ": " + e.getMessage());
        }

        return interactions;
    }





    // Store user recommendations score with interested category
    public void storeRecommendations(int userID, List<Map.Entry<String, Double>> sortedCategoryScores) {
        String query = "INSERT INTO RecommendationTable (UserID, Category, CategoryScore, DateAndTime) VALUES (?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (Map.Entry<String, Double> entry : sortedCategoryScores) {
                String category = entry.getKey(); // Category from the entry
                double categoryScore = entry.getValue(); // Associated score

                // Format current date and time as 'yyyy-MM-dd HH:mm:ss'
                String formattedDateTime = DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss")
                        .format(LocalDateTime.now());

                // Set the values for the prepared statement
                statement.setInt(1, userID);          // UserID
                statement.setString(2, category);     // Category
                statement.setDouble(3, categoryScore); // CategoryScore
                statement.setString(4, formattedDateTime); // Date and Time in the specified format

                // Add to batch for efficiency
                statement.addBatch();
            }

            // Execute batch insert to store all recommendations
            statement.executeBatch();


        } catch (SQLException e) {
            System.out.println("Error saving recommendations: " + e.getMessage());
        }
    }





    // Return article category by article ID
    public String getCategoryByArticleID(int articleID) {
        String query = "SELECT Category FROM ArticleTable WHERE ArticleID = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, articleID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("Category");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching category for articleID " + articleID + ": " + e.getMessage());
        }
        return null; // Return null if no category is found
    }





    // Retrieve category scores for a user
    public Map<String, Integer> getCategoryScores(int userID) {
        String query = "SELECT Category, SUM(CategoryScore) AS TotalScore " +
                "FROM RecommendationTable " +
                "WHERE UserID = ? " +
                "GROUP BY Category";
        Map<String, Integer> categoryScores = new HashMap<>();

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String category = rs.getString("Category");
                int score = rs.getInt("TotalScore");
                categoryScores.put(category, score);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving category scores for UserID: " + userID + " - " + e.getMessage());
        }

        return categoryScores;
    }





    // Retrieve articles by a list of categories
    public List<Map<String, Object>> getArticlesByCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            placeholders.append("?");
            if (i < categories.size() - 1) {
                placeholders.append(", ");
            }
        }

        String query = "SELECT ArticleID, Headline, Description, Category, NewsImagePath " +
                "FROM ArticleTable " +
                "WHERE Category IN (" + placeholders + ")";

        List<Map<String, Object>> articles = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < categories.size(); i++) {
                statement.setString(i + 1, categories.get(i));
            }

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Map<String, Object> article = new HashMap<>();
                article.put("ArticleID", rs.getInt("ArticleID"));
                article.put("Headline", rs.getString("Headline"));
                article.put("Description", rs.getString("Description"));
                article.put("Category", rs.getString("Category"));
                article.put("NewsImagePath", rs.getString("NewsImagePath"));
                articles.add(article);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving articles for categories: " + categories + " - " + e.getMessage());
        }

        return articles;
    }


}











