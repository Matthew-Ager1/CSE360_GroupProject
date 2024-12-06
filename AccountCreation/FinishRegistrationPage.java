package AccountCreation;

import Core.Navigation;
import Database.UsersAPI;
import Database.Models.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class FinishRegistrationPage {
    public static void RegisterWithNavigation(User user) {

        System.out.println("User Information upon initialization:");
        System.out.println("Id: " + user.getId());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Username: " + user.getUsername());
        System.out.println("First Name: " + user.getFirstName());
        System.out.println("Middle Name: " + user.getMiddleName());
        System.out.println("Last Name: " + user.getLastName());
        System.out.println("Is Finished: " + user.getIsFinished());

        Label titleLabel = new Label("Finish Setting Up Your Account");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter your first name");

        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Enter your middle name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter your last name");

        TextField preferredNameField = new TextField();
        preferredNameField.setPromptText("Enter your preferred name (Optional)");

        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button submitButton = new Button("Finish Setup");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        submitButton.setPadding(new Insets(10, 20, 10, 20));

        submitButton.setOnAction((ActionEvent event) -> {
            if (user == null) {
                errorMessageLabel.setText("User is not initialized.");
                return;
            }

            String email = emailField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String middleName = middleNameField.getText();
            String preferredName = preferredNameField.getText();

            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMiddleName(middleName);
            user.setPreferredName(preferredName);

            user.setIsFinished(true);

            System.out.println("User Information before update:");
            System.out.println("Email: " + user.getEmail());
            System.out.println("First Name: " + user.getFirstName());
            System.out.println("Middle Name: " + user.getMiddleName());
            System.out.println("Last Name: " + user.getLastName());
            System.out.println("Password: " + user.getPassword());
            System.out.println("Is Finished: " + user.getIsFinished());

            boolean success = UsersAPI.updateUser(user);

            if (success) {
                Navigation.navigateTo("LoginPage", null);
            } else {
                errorMessageLabel.setText("Failed to update account. Try again.");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, emailField, firstNameField, middleNameField, lastNameField, preferredNameField, errorMessageLabel, submitButton);

        Scene scene = new Scene(layout, 350, 400);
        Navigation.registerScene("FinishRegistrationPage", scene);
    }
}