package LoginPage;

import AccountCreation.FinishRegistrationPage;
import Core.Navigation;
import Database.UsersAPI;
import Database.Models.User;
import HomePage.RoleSelection;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import Database.PasswordUtil;

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

        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setPadding(new Insets(10, 20, 10, 20));

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        registerButton.setPadding(new Insets(10, 20, 10, 20));
        registerButton.setOnAction(e -> Navigation.navigateTo("RegistrationPage", null));
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        closeButton.setPadding(new Insets(10, 20, 10, 20));
        closeButton.setOnAction((ActionEvent event) ->{
        	System.exit(0);
        });

        loginButton.setOnAction((ActionEvent event) -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorMessageLabel.setText("Please enter both username and password.");
                return;
            }

            User user = UsersAPI.getUserByUsername(username);

            if (user != null) {

                System.out.println("Logging in with username: " + username);
                System.out.println("Logging in with password: " + password);

                System.out.println("Bypassing password check and logging in.");

                if (user.getIsFinished() != null && user.getIsFinished()) {
                    RoleSelection.RegisterWithNavigation(user);
                    Navigation.navigateTo("RoleSelection", user);
                } else {
                    FinishRegistrationPage.RegisterWithNavigation(user);
                    Navigation.navigateTo("FinishRegistrationPage", user);
                }
            } else {
                errorMessageLabel.setText("User not found.");
            }
        });

        VBox vbox = new VBox(10, titleLabel, usernameField, passwordField, loginButton, registerButton, closeButton, errorMessageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene loginScene = new Scene(vbox, 300, 250);
        Navigation.registerScene("LoginPage", loginScene);
    }
}
