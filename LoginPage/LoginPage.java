package LoginPage;

import Core.Navigation;
import Database.UsersAPI;
import Database.Models.User;
import AccountCreation.RegistrationPage;
import HomePage.AdminHomePage;
import HomePage.InstructorHomePage;
import HomePage.UserHomePage;
import HomePage.RoleSelection;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginPage {
    public static void RegisterWithNavigation() {
        Label titleLabel = new Label("Welcome, Please Log In");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(200);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setPadding(new Insets(10, 20, 10, 20));
        loginButton.setOnAction((ActionEvent event) -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (UsersAPI.isValidLogin(username, password)) {
                User loggedInUser = UsersAPI.getUser(username);
                if (loggedInUser != null) {
                    System.out.println("User retrieved: " + loggedInUser.getName());
                    // Redirect to RoleSelection page
                    RoleSelection.RegisterWithNavigation(loggedInUser);
                    Navigation.navigateTo("RoleSelection");
                } else {
                    System.out.println("Failed to retrieve User");
                }
            } else {
                System.out.println("Login failed, redirecting to RegistrationPage");
                RegistrationPage.show(Navigation.getPrimaryStage(), username, password);
            }
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton);

        Scene scene = new Scene(layout, 350, 250);
        Navigation.registerScene("LoginPage", scene);
    }

    public static void handleRoleBasedNavigation(User user) {
        String highestRole = user.getRoles().stream().max(String::compareTo).orElse("student");

        switch (highestRole) {
            case "admin":
                AdminHomePage.RegisterWithNavigation(user);
                Navigation.navigateTo("AdminHomePage");  // Ensure this matches the registration in AdminHomePage
                break;
            case "instructor":
                InstructorHomePage.RegisterWithNavigation(user);
                Navigation.navigateTo("InstructorHomePage");
                break;
            default:
                UserHomePage.RegisterWithNavigation(user);
                Navigation.navigateTo("UserHomePage");
                break;
        }
    }
}
