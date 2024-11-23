package HomePage;

import Core.Navigation;
import Database.GroupsAPI;
import Database.UsersAPI;
import Database.Models.Role;
import Database.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ManageUsersPage {

    public static void RegisterWithNavigation(List<User> users) {
        // Title
        Label titleLabel = new Label("Manage Users");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ListView to display users
        ListView<String> usersListView = new ListView<>();
        refreshUsersList(usersListView, users);

        // Buttons
        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(e -> {
            showAddUserDialog();
            refreshUsersList(usersListView, users);
        });

        Button changeRolesButton = new Button("Change Roles");
        changeRolesButton.setOnAction(e -> {
            String selectedUser = usersListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String username = selectedUser.split(" - ")[0];
                showChangeRolesDialog(username);
                refreshUsersList(usersListView, users);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to change roles.");
            }
        });

        Button removeUserButton = new Button("Delete User");
        removeUserButton.setOnAction(e -> {
            String selectedUser = usersListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String username = selectedUser.split(" - ")[0];
                boolean confirmed = showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete user: " + username + "?");
                if (confirmed) {
                    boolean success = UsersAPI.deleteUserByUsername(username);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failure", "Failed to delete user.");
                    }
                    refreshUsersList(usersListView, users);
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to remove.");
            }
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Navigation.navigateTo("AdminHomePage");
        });

        // Layout for buttons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(addUserButton, changeRolesButton, removeUserButton, backButton);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, usersListView, buttonsBox);

        Scene scene = new Scene(layout, 600, 500);
        Navigation.registerScene("ManageUsersPage", scene);
    }

    private static void refreshUsersList(ListView<String> usersListView, List<User> users) {
        ObservableList<String> userStrings = FXCollections.observableArrayList();
        for (User user : users) {
            userStrings.add(user.getUsername() + " - " + user.getRoles());
        }
        usersListView.setItems(userStrings);
    }

    private static void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter user details:");

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the fields
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        ComboBox<Role> roleComboBox = new ComboBox<>();
        roleComboBox.setItems(FXCollections.observableArrayList(Role.ADMIN, Role.INSTRUCTOR, Role.STUDENT));
        roleComboBox.setValue(Role.STUDENT);

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Email:"), emailField,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Full Name:"), nameField,
                new Label("Role:"), roleComboBox
        );
        dialog.getDialogPane().setContent(content);

        // Convert the result to a User when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String email = emailField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String name = nameField.getText();
                Role role = roleComboBox.getValue();

                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "All fields must be filled.");
                    return null;
                }

                // Use correct constructor with Set<Role>
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                return new User(email, username, password, name, false, 0, roles, new ArrayList<>());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            boolean success = UsersAPI.addUser(user);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to add user.");
            }
        });
    }

    private static void showChangeRolesDialog(String username) {
        User user = UsersAPI.getUserByUsername(username);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "User Not Found", "The selected user could not be found.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Change Roles");
        dialog.setHeaderText("Select roles for user: " + user.getName());

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create checkboxes for each role
        CheckBox adminCheckBox = new CheckBox("Admin");
        CheckBox instructorCheckBox = new CheckBox("Instructor");
        CheckBox studentCheckBox = new CheckBox("Student");

        // Set selected based on current roles
        Set<Role> roles = user.getRoles();
        adminCheckBox.setSelected(roles.contains(Role.ADMIN));
        instructorCheckBox.setSelected(roles.contains(Role.INSTRUCTOR));
        studentCheckBox.setSelected(roles.contains(Role.STUDENT));

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Roles:"),
                adminCheckBox,
                instructorCheckBox,
                studentCheckBox
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Set<Role> newRoles = new HashSet<>();
                if (adminCheckBox.isSelected()) {
                    newRoles.add(Role.ADMIN);
                }
                if (instructorCheckBox.isSelected()) {
                    newRoles.add(Role.INSTRUCTOR);
                }
                if (studentCheckBox.isSelected()) {
                    newRoles.add(Role.STUDENT);
                }
                if (newRoles.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "At least one role must be selected.");
                    return null;
                }

                boolean success = UsersAPI.updateUserRoles(user.getUsername(), newRoles);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User roles updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failure", "Failed to update user roles.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}

