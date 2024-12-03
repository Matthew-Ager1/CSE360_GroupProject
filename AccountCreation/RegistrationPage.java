package AccountCreation;

import Core.Navigation;
import Database.PasswordUtil;
import Database.UsersAPI;
import Database.Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegistrationPage {
	public static void RegisterWithNavigation() {
		Label usernameLabel = new Label("Username:");
		Label passwordLabel = new Label("Password:");
		Label emailLabel = new Label("Email:");
        Label firstNameLabel = new Label("First Name:");
        Label middleNameLabel = new Label("Middle Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label preferredNameLabel = new Label("Preferred Name:");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");

        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Enter middle name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");

        TextField preferredNameField = new TextField();
        preferredNameField.setPromptText("Enter preferred name");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        
        TextField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitButton.setOnAction(event -> {
            String email = emailField.getText();
            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String preferredName = preferredNameField.getText();
            String password = passwordField.getText();
            String username = usernameField.getText();
            
            if (!email.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
                System.out.println("Account created for: " + username + ", " + firstName + " " + lastName);
                password = PasswordUtil.hashPassword(password);
                User user = new User();
                user.setEmail(email);
                user.setName(firstName + " " + middleName + " " + lastName);
                user.setPassword(password);
                user.setUsername(username);
                UsersAPI.addUser(user);
                Navigation.navigateTo("UserHomePage");
            }
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(middleNameLabel, 0, 2);
        grid.add(middleNameField, 1, 2);
        grid.add(lastNameLabel, 0, 3);
        grid.add(lastNameField, 1, 3);
        grid.add(preferredNameLabel, 0, 4);
        grid.add(preferredNameField, 1, 4);
        grid.add(usernameLabel, 0, 5);
        grid.add(usernameField, 1, 5);
        grid.add(passwordLabel, 0, 6);
        grid.add(passwordField, 1, 6);
        grid.add(submitButton, 1, 7);

        Scene scene = new Scene(grid, 400, 350);

    	Navigation.registerScene("RegistrationPage", scene);
    }
}
