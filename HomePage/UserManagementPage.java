package HomePage;

import Core.Navigation;
import Database.Models.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.UUID;

public class UserManagementPage {
    public static void RegisterWithNavigation(List<User> users) {
        Label titleLabel = new Label("User Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ListView<User> userListView = new ListView<>();
        userListView.getItems().addAll(users);

        // Reset Password Button
        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.setOnAction((ActionEvent event) -> {
            User selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                resetPassword(selectedUser);
            }
        });

        // Delete User Button
        Button deleteUserButton = new Button("Delete User");
        deleteUserButton.setOnAction((ActionEvent event) -> {
            User selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                showDeleteConfirmation(selectedUser);
            }
        });

        // Add/Remove Roles Button
        Button modifyRolesButton = new Button("Add/Remove Roles");
        modifyRolesButton.setOnAction((ActionEvent event) -> {
            User selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                showModifyRolesPopup(selectedUser);
            }
        });

        // Generate Code Button
        Button generateCodeButton = new Button("Generate Code");
        generateCodeButton.setOnAction((ActionEvent event) -> {
            showGenerateCodePopup();
        });

        HBox buttonLayout = new HBox(10, resetPasswordButton, deleteUserButton, modifyRolesButton, generateCodeButton);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(10));

        VBox layout = new VBox(15, titleLabel, userListView, buttonLayout);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 400);
        Navigation.registerScene("UserManagementPage", scene);
    }

    private static void resetPassword(User user) {
        // Implement your logic to reset the password for the user
        System.out.println("Reset password for user: " + user.getName());
    }

    private static void showDeleteConfirmation(User user) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Delete User");

        Label message = new Label("Are you sure you want to delete this user?");
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            deleteUser(user);
            popup.close();
        });
        Button noButton = new Button("No");
        noButton.setOnAction(e -> popup.close());

        HBox buttons = new HBox(10, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, message, buttons);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 150);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private static void deleteUser(User user) {
        // Implement your logic to delete the user
        System.out.println("Deleted user: " + user.getName());
    }

    private static void showModifyRolesPopup(User user) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Modify Roles");

        Label instruction = new Label("Select roles for " + user.getName());
        CheckBox adminCheckBox = new CheckBox("Admin");
        CheckBox instructorCheckBox = new CheckBox("Instructor");
        CheckBox studentCheckBox = new CheckBox("Student");

        // Set selected based on current roles
        adminCheckBox.setSelected(user.getRoles().contains("admin"));
        instructorCheckBox.setSelected(user.getRoles().contains("instructor"));
        studentCheckBox.setSelected(user.getRoles().contains("user"));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            user.getRoles().clear();
            if (adminCheckBox.isSelected()) user.getRoles().add("admin");
            if (instructorCheckBox.isSelected()) user.getRoles().add("instructor");
            if (studentCheckBox.isSelected()) user.getRoles().add("user");
            // Save user roles to database
            System.out.println("Updated roles for user: " + user.getName());
            popup.close();
        });

        VBox layout = new VBox(10, instruction, adminCheckBox, instructorCheckBox, studentCheckBox, saveButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 250);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private static void showGenerateCodePopup() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Generate Code");

        Label instruction = new Label("Select roles for the new user");
        CheckBox adminCheckBox = new CheckBox("Admin");
        CheckBox instructorCheckBox = new CheckBox("Instructor");
        CheckBox studentCheckBox = new CheckBox("Student");

        TextField codeField = new TextField();
        codeField.setEditable(false);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            StringBuilder roles = new StringBuilder();
            if (adminCheckBox.isSelected()) roles.append("A"); // A for Admin
            if (instructorCheckBox.isSelected()) roles.append("I"); // I for Instructor
            if (studentCheckBox.isSelected()) roles.append("S"); // S for Student

            String code = generateCode(roles.toString());
            codeField.setText(code);
        });

        VBox layout = new VBox(10, instruction, adminCheckBox, instructorCheckBox, studentCheckBox, generateButton, codeField);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private static String generateCode(String roles) {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        String combinedString = uuid + roles;
        return combinedString.length() > 8 ? combinedString.substring(0, 8) : combinedString;
    }
}
