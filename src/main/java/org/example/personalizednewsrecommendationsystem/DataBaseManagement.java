package org.example.personalizednewsrecommendationsystem;

import java.sql.*;

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
}
