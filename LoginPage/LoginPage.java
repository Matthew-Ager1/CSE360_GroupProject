package LoginPage;

import AccountCreation.RegistrationPage;
import Core.Navigation;
import Database.UsersAPI;
import Database.Models.User;
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
import javafx.stage.Stage;

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

        // Login button with improved styling
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setPadding(new Insets(10, 20, 10, 20));
        loginButton.setOnAction((ActionEvent event) -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            // Add database logic
            if (UsersAPI.isValidLogin(username, password)) {

            	RoleSelection.RegisterWithNavigation();
            	
            	Navigation.navigateTo("RoleSelection");
            } else {
            	System.out.println("Invalid User/Pass");
            }
            
            
        });

        // Layout adjustments
        VBox layout = new VBox(15);  // Increased spacing between elements
        layout.setPadding(new Insets(20));  // Add padding around the layout
        layout.setAlignment(Pos.CENTER);  // Center the elements
        layout.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton);
    	
        Scene scene = new Scene(layout, 350, 250);
    	
    	Navigation.registerScene("LoginPage", scene);
    }
}