package org.example.personalizednewsrecommendationsystem;

public class UserManagement {

    public String username;
    public String password;
    private final DataBaseManagement dbHandler;
    private final Registration_Scene_Controller registrationController;

    public UserManagement(DataBaseManagement dbHandler, Registration_Scene_Controller registrationController) {
        this.dbHandler = dbHandler;
        this.registrationController = registrationController;
    }

    // Registers a new user
    public String registerUser() {
        username = registrationController.getRegisteredUserName().getText();
        password = registrationController.getRegisteredUserPassword().getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "Username and Password cannot be empty *";
        }

        if (dbHandler.checkUserExists(username, password)) {
            return "Username already exists *";
        }

        dbHandler.insertUser(username, password);
        return "User registered successfully!";
    }

    // Authenticates an existing user
    public boolean authenticateUser(String username, String password) {
        return dbHandler.checkUserExists(username, password);
    }


}
