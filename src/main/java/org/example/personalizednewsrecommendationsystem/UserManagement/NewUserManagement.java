package org.example.personalizednewsrecommendationsystem.UserManagement;

import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;
import org.example.personalizednewsrecommendationsystem.GUIContollers.LoginController;
import org.example.personalizednewsrecommendationsystem.GUIContollers.RegistrationController;

import javafx.application.Platform;


public class NewUserManagement extends UserManagement {
    private final String newUsername;  // Registration username
    private final String newUserPassword;  // Registration user password

    private final RegistrationController registrationController;  // Registration GUI controller class
    private final DatabaseManagement databaseManagement;  // Database management class
    private final ConcurrencyManagement concurrencyManagement;  // Concurrency management class



    //Constructor to initialize user management with the necessary controllers
    public NewUserManagement(RegistrationController registrationController,
                             LoginController loginController,
                             DatabaseManagement databaseManagement,
                             ConcurrencyManagement concurrencyManagement) {
        super(loginController,databaseManagement,concurrencyManagement);
        this.registrationController = registrationController;
        this.databaseManagement = databaseManagement;
        this.concurrencyManagement = concurrencyManagement;
        this.newUsername = registrationController.getRegisteredUserName();
        this.newUserPassword = registrationController.getRegisteredUserPassword();
    }



    // Registers a new user by validating inputs and interacting with the database.
    public void register() {
        // Execute the task in the background thread pool
        concurrencyManagement.handleTask(() -> {
            String message; // Variable to hold the registration result message

            try {
                // Validate username and password
                if (newUsername == null || newUsername.isEmpty() || newUserPassword == null || newUserPassword.isEmpty()) {
                    message = "Username and password cannot be empty!";
                } else if (databaseManagement.checkUserExists(newUsername,newUserPassword)) {
                    message = "User already exists!";
                } else if (databaseManagement.checkUserName(newUsername)) {
                    message = "Choose another username!";
                } else if (!isPasswordValid(newUserPassword)) {
                    message = "Password must be 6 chars with a digit!";
                } else {
                    // Register the user in the database
                    databaseManagement.storeUserDetails(newUsername,newUserPassword);
                    message = "Registration successful!";
                }
            } catch (Exception e) {
                message = "An error occurred: " + e.getMessage();
            }

            // Update the UI safely on the JavaFX Application Thread
            String finalMessage = message;
            Platform.runLater(() -> registrationController.setRegistrationMessage(finalMessage));
        });
    }



    // A method to validate the password.
    private boolean isPasswordValid(String password) {
        if (password.length() <= 5) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasLetter && hasDigit;
    }



    // Getter method for call registered username and password
    public String getNewUsername() {
        return newUsername;
    }


    public String getNewUserPassword() {
        return newUserPassword;
    }



}
