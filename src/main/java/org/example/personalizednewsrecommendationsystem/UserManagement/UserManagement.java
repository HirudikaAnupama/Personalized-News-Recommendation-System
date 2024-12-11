package org.example.personalizednewsrecommendationsystem.UserManagement;

import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;
import org.example.personalizednewsrecommendationsystem.GUIContollers.LoginController;
import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;

import javafx.application.Platform;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserManagement {

    private int userID;  // Login user ID
    private String username;  // Login username
    private String password;  // Login user password

    private final LoginController loginController;  // Logging GUI controller class
    private final DatabaseManagement databaseManagement;  // Database management class
    private final ConcurrencyManagement concurrencyManagement;  // Concurrency management class



    //Constructor to initialize user management with the necessary controllers
    public UserManagement(LoginController loginController,
                          DatabaseManagement databaseManagement,
                          ConcurrencyManagement concurrencyManagement) {
        this.loginController = loginController;
        this.databaseManagement = databaseManagement;
        this.username = loginController.getLoggingUserName();
        this.password = loginController.getLoggingUserPassword();
        this.userID = databaseManagement.getUserID(username);
        this.concurrencyManagement = concurrencyManagement;
    }



    // Login users by validating inputs and interacting with the database.
    public boolean login() {
        // Ensure username and password are valid
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            updateLoginMessage("Username and password cannot be empty!");
            return false;
        }

        try {
            // Prepare and submit a task to check the database
            Future<Boolean> future = concurrencyManagement.getExecutorService().submit(() -> {
                try {
                    // Check if user exists in the database
                    if (!databaseManagement.checkUserExists(username, password)) {
                        updateLoginMessage("User does not exist. Please register!");
                        return false;
                    }
                    // Login successful
                    return true;
                } catch (Exception e) {
                    updateLoginMessage("An error occurred during login: " + e.getMessage());
                    return false;
                }
            });

            // Block until the result is available
            return future.get();

        } catch (InterruptedException | ExecutionException e) {
            updateLoginMessage("An error occurred during login: " + e.getMessage());
            return false;
        }
    }



    private void updateLoginMessage(String message) {
        Platform.runLater(() -> loginController.getLoggingMessage().setText(message));
    }



    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }



    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
