package LoginPage;

import Core.Navigation;
import Database.UsersAPI;
import Database.Models.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import HomePage.*;

public class LoginPage {
    public static void RegisterWithNavigation() {
        // Create a label for the page title
        Label titleLabel = new Label("Welcome, Please Log In");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Username field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(200);

        // Password field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(200);

        // Error message label
        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Login button with improved styling
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setPadding(new Insets(10, 20, 10, 20));

        // Register button with improved styling
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px;");
        registerButton.setPadding(new Insets(10, 20, 10, 20));
        registerButton.setOnAction(e -> {
            Navigation.navigateTo("RegistrationPage");
        });

        loginButton.setOnAction((ActionEvent event) -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                errorMessageLabel.setText("Please enter both username and password.");
                return;
            }

            // Add database logic
            if (UsersAPI.isValidLogin(username, password)) {
                User user = UsersAPI.getUserByUsername(username);
                if (user != null) {
                    // Updated the logic based on user's roles
                    if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                        // Navigate based on the first role (for simplicity)
                        switch (user.getRoles().iterator().next()) {
                            case ADMIN:
                                AdminHomePage.RegisterWithNavigation(user); // Pass the user object
                                Navigation.navigateTo("AdminHomePage");
                                break;
                            case INSTRUCTOR:
                                InstructorHomePage.RegisterWithNavigation(user); // Pass the user object
                                Navigation.navigateTo("InstructorHomePage");
                                break;
                            case STUDENT:
                                StudentHomePage.RegisterWithNavigation(user); // Pass the user object
                                Navigation.navigateTo("StudentHomePage");
                                break;
                            default:
                                errorMessageLabel.setText("Unknown user role.");
                                break;
                        }
                    }
                } else {
                    errorMessageLabel.setText("Username/password incorrect.");
                }
            } else {
                errorMessageLabel.setText("Username/password incorrect.");
            }
        });

        // Layout adjustments
        VBox layout = new VBox(10);  // Reduced spacing between elements
        layout.setPadding(new Insets(20));  // Add padding around the layout
        layout.setAlignment(Pos.CENTER);  // Center the elements

        // Add the elements in order: Title, fields, error message, buttons
        layout.getChildren().addAll(titleLabel, usernameField, passwordField, errorMessageLabel, loginButton, registerButton);

        Scene scene = new Scene(layout, 350, 250);
        Navigation.registerScene("LoginPage", scene);
    }
}