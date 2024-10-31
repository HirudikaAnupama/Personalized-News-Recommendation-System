package org.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class UserManagement {

    @FXML
    private TextField loggingUserName;
    @FXML
    private TextField loggingUserPassword;

    @FXML
    private TextField registeredUserName;
    @FXML
    private TextField registeredUserPassword;

    @FXML
    private Label registrationMSG;

    // Database path
    private static final String URL = "jdbc:sqlite:C:/Users/LENOVO/Desktop/Lectures 3/OOD/CourseWork/Personalized-News-Recommendation-System/news_recommendation.db";

    // Method to establish a connection to the SQLite database
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

    // Method to handle the registration button click event
    public void onRegisterButtonConfirm(ActionEvent event) {
        String username = registeredUserName.getText();
        String password = registeredUserPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            registrationMSG.setText("Username and password cannot be empty.");
            return;
        }

        // Optional: Hash the password for security
        String hashedPassword = hashPassword(password); // Replace this with actual hashing if needed

        // Check if the username and password already exist
        if (checkUserExists(username, hashedPassword)) {
            registrationMSG.setText("Username and password already exist. Please choose another.");
            return;
        }

        // SQL insertion query
        String insertSQL = "INSERT INTO UserDetails (UserName, UserPassword) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // Set parameters in SQL
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);

            // Execute the insert statement
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                registrationMSG.setText("User registered successfully!");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                registrationMSG.setText("Username already taken. Please choose another.");
            } else {
                registrationMSG.setText("Registration failed: " + e.getMessage());
            }
        }
    }

    // Method to check if the user already exists in the database
    private boolean checkUserExists(String username, String password) {
        String selectSQL = "SELECT COUNT(*) FROM UserDetails WHERE UserName = ? AND UserPassword = ?";
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



    // Placeholder for a password hashing method
    private String hashPassword(String password) {
        // Implement password hashing here if desired (e.g., SHA-256 or bcrypt)
        return password; // Replace this with actual hashed password for production
    }



    @FXML
    private void onRegisterButtonClick(ActionEvent event) throws IOException {
        // Access the current stage from the button source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the Registration Scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Registration-Scene.fxml")));
        // Set the scene, dimensions, and title for the Registration window
        stage.setScene(new Scene(parent, 700, 500));
        stage.setTitle("NewsFlow");
    }


    public void onClickBackButtonInRegistration(ActionEvent event) throws IOException {

        // Access the current stage from the button source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the Registration Scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login-Scene.fxml")));
        // Set the scene, dimensions, and title for the Registration window
        stage.setScene(new Scene(parent, 700, 500));
        stage.setTitle("NewsFlow");

    }

    public void onLoginSuccess(ActionEvent event) throws IOException {
        // Access the current stage from the button source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the Registration Scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main-Dashboard-Scene.fxml")));
        // Set the scene, dimensions, and title for the Registration window
        stage.setScene(new Scene(parent, 700, 500));
        stage.setTitle("NewsFlow");

    }


}
