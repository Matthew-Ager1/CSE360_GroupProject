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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PasswordResetPage {
	public static void show(Stage primaryStage, User user) {
        Label passwordLabel = new Label("New Password:");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter email");
        
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitButton.setOnAction(event -> {
            String password = passwordField.getText();
            if (!password.isEmpty()) {
                System.out.println("Password Updated");
                user.setPassword(password);
                password = PasswordUtil.hashPassword(password);
                UsersAPI.updateUserPassword(user.getUsername(), password);
                Navigation.navigateTo("UserHomePage");
            }
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(passwordLabel, 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(submitButton, 0, 1);

        Scene scene = new Scene(grid, 400, 350);

    	primaryStage.setScene(scene);
    }
}
