package AccountCreation;

import Core.Navigation;
import Database.UsersAPI;
import Database.Models.User;
import Database.Models.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import Database.PasswordUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegistrationPage {
    public static void RegisterWithNavigation() {
        Scene registrationScene = createRegistrationScene();
        Navigation.registerScene("RegistrationPage", registrationScene);
    }

    private static Scene createRegistrationScene() {
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label confirmPasswordLabel = new Label("Confirm Password:");
        Label invitationCodeLabel = new Label("Invitation Code:");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");

        Label passwordMatchLabel = new Label();
        passwordMatchLabel.setStyle("-fx-text-fill: red;");

        ChangeListener<String> passwordValidationListener = (observable, oldValue, newValue) -> {
            String message = passwordChecker(passwordField.getText(), confirmPasswordField.getText());
            passwordMatchLabel.setText(message);
            if (message.equals("Passwords match.")) {
                passwordMatchLabel.setStyle("-fx-text-fill: green;");
            } else {
                passwordMatchLabel.setStyle("-fx-text-fill: red;");
            }
        };

        passwordField.textProperty().addListener(passwordValidationListener);
        confirmPasswordField.textProperty().addListener(passwordValidationListener);

        TextField invitationCodeField = new TextField();
        invitationCodeField.setPromptText("Enter invitation code");

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitButton.setOnAction(event -> {
            String username = usernameField.getText();
            String enteredPassword = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String invitationCode = invitationCodeField.getText();

            if (invitationCode.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Form Error!", "Please enter the invitation code.");
                return;
            }

            if (username.isEmpty() || enteredPassword.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Form Error!", "Please enter all required fields.");
                return;
            }

            if (!passwordChecker(enteredPassword, confirmPassword).equals("Passwords match.")) {
                showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match. Please try again.");
                return;
            }

            List<User> users = UsersAPI.getAllUsers();
            for (User existingUser : users) {
                if (existingUser != null && existingUser.getUsername() != null && existingUser.getUsername().trim().equalsIgnoreCase(username)) {
                    showAlert(Alert.AlertType.ERROR, "User Exists", "A user with that username already exists. Please choose a different username.");
                    return;
                }
            }

            String hashedPassword = PasswordUtil.hashPassword(enteredPassword);

            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword);

            Set<Role> roles = new HashSet<>();
            if (invitationCode.contains("a")) roles.add(Role.ADMIN);
            if (invitationCode.contains("i")) roles.add(Role.INSTRUCTOR);
            if (invitationCode.contains("s")) roles.add(Role.STUDENT);
            user.setRoles(roles);

            boolean userAdded = UsersAPI.addUser(user);
            if (!userAdded) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "There was an error saving your user. Please try again.");
                return;
            }

            Navigation.navigateTo("LoginPage", null);
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(passwordMatchLabel, 1, 3);
        grid.add(invitationCodeLabel, 0, 4);
        grid.add(invitationCodeField, 1, 4);
        grid.add(submitButton, 1, 5);

        return new Scene(grid, 400, 300);
    }

    public static String passwordChecker(String password, String confirmPassword) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return "";
        }
        return password.equals(confirmPassword) ? "Passwords match." : "Passwords do not match.";
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
