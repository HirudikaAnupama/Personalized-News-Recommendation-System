package org.example.personalizednewsrecommendationsystem;

public class UserManagement {
    private final DataBaseManagement dbHandler;

    public UserManagement(DataBaseManagement dbHandler) {
        this.dbHandler = dbHandler;
    }

    // Registers a new user
    public String registerUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return "Username and Password cannot be empty *";
        }

        if (dbHandler.checkUserExists(username, password)) {
            return "Username already exists *";
        }

        dbHandler.insertUser(username, password);
        return "User registered successfully !";
    }

    // Authenticates an existing user
    public boolean authenticateUser(String username, String password) {
        return dbHandler.checkUserExists(username, password);
    }
}
