package org.example.personalizednewsrecommendationsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    public List<String> getHeadlines() {
        List<String> headlines = new ArrayList<>();
        String query = "SELECT headline FROM Articles";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                headlines.add(rs.getString("headline"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching headlines: " + e.getMessage());
        }
        return headlines;
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



}
