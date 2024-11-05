package org.example.personalizednewsrecommendationsystem;

import java.sql.*;

public class DataBaseManagement {

    // Database path
    private static final String URL = "jdbc:sqlite:NewsFlow_News_Recommendation_System_DataBase.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connected to the SQLite database.");
        } catch (SQLException e) {
            System.out.println("Connection to SQLite failed: " + e.getMessage());
        }
        return conn;
    }

    // Method to check if the user already exists in the database
    public boolean checkUserExists(String username, String password) {
        String selectSQL = "SELECT COUNT(*) FROM UserTabel WHERE UserName = ? AND UserPassword = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false; // User does not exist
    }

    // Placeholder for a password hashing method
    private String hashPassword(String password) {
        // Implement password hashing here if desired (e.g., SHA-256 or bcrypt)
        return password; // Replace this with actual hashed password for production
    }

    // Method to input user details, taking Registration_Scene_Controller as a parameter
    public void inputUserDetails(Registration_Scene_Controller controller) {
        String userName = controller.getRegisteredUserName().getText();
        String userPassword = controller.getRegisteredUserPassword().getText();


        if (userName.isEmpty() || userPassword.isEmpty()) {
            controller.getRegistrationMessage().setText("Username and password cannot be empty.");
            return;
        }

        // Optional: Hash the password for security
        String hashedPassword = hashPassword(userPassword);

        // Check if the username and password already exist
        if (checkUserExists(userName, hashedPassword)) {
            controller.getRegistrationMessage().setText("Username and password already exist. Please choose another.");
            return;
        }

        // Insert the new user details with auto-generated userID
        String insertSQL = "INSERT INTO UserTabel (UserName, UserPassword) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            controller.getRegistrationMessage().setText("User registered successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting user details: " + e.getMessage());
        }

    }


}
