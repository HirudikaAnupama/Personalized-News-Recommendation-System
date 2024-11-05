package org.example.personalizednewsrecommendationsystem;

public class UserManagement {
    private Login_Scene_Controller controller;

    // Constructor to set the controller
    public UserManagement(Login_Scene_Controller controller) {
        this.controller = controller;
    }

    public void check() {
        String name = controller.getLoggingUserName().getText();
        String password = controller.getLoggingUserPassword().getText();

        if (name.isEmpty() || password.isEmpty()) {
            controller.getLoggingMessage().setText("Empty");
        }
    }
}
