package LoginPage;

import Core.Navigation;
import Database.UsersAPI;
import Database.Models.Role;
import Database.Models.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
            
            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter both username and password.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Add database logic
            if (UsersAPI.isValidLogin(username, password)) {
            	User user = UsersAPI.getUserByUsername(username);
            	if (user != null) {
            	    switch (user.getRole()) {
            	        case ADMIN:
            	            Navigation.navigateTo("AdminHomePage");
            	            break;
            	        case INSTRUCTOR:
            	            Navigation.navigateTo("InstructorHomePage");
            	            break;
            	        case STUDENT:
            	            Navigation.navigateTo("StudentHomePage");
            	            break;
            	        default:
            	            Alert alert = new Alert(Alert.AlertType.ERROR, "Unknown user role.", ButtonType.OK);
            	            alert.showAndWait();
            	            break;
            	    }
            	} else {
            	    Alert alert = new Alert(Alert.AlertType.ERROR, "User not found.", ButtonType.OK);
            	    alert.showAndWait();
            	}
            } else {
            	Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.", ButtonType.OK);
            	alert.showAndWait();
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
