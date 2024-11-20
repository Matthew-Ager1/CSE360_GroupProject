package HomePage;

import Core.Navigation;
import Database.GroupsAPI;
import Database.UsersAPI;
import Database.Models.Group;
import Database.Models.Role;
import Database.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageUsersPage {
    public static void RegisterWithNavigation() {
        // Title
        Label titleLabel = new Label("Manage Users");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ListView to display users
        ListView<String> usersListView = new ListView<>();
        refreshUsersList(usersListView);

        // Buttons
        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(e -> {
            showAddUserDialog();
            refreshUsersList(usersListView);
        });

        Button assignGroupButton = new Button("Assign to Group");
        assignGroupButton.setOnAction(e -> {
            String selectedUser = usersListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String username = selectedUser.split(" - ")[0];
                showAssignGroupDialog(username);
                refreshUsersList(usersListView);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to assign.");
            }
        });

        Button removeUserButton = new Button("Remove User");
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
                    refreshUsersList(usersListView);
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
        buttonsBox.getChildren().addAll(addUserButton, assignGroupButton, removeUserButton, backButton);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, usersListView, buttonsBox);

        Scene scene = new Scene(layout, 600, 500);
        Navigation.registerScene("ManageUsersPage", scene);
    }

    private static void refreshUsersList(ListView<String> usersListView) {
        List<User> users = UsersAPI.getAllUsers();
        ObservableList<String> userStrings = FXCollections.observableArrayList();
        for (User user : users) {
            userStrings.add(user.getUsername() + " - " + user.getRole().toString());
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

                return new User(email, username, password, name, false, 0, role, new ArrayList<>());
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

    private static void showAssignGroupDialog(String username) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Assign User to Group");
        dialog.setHeaderText("Select a group to assign to user: " + username);

        // Set the button types.
        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        // Create the fields
        ComboBox<String> groupComboBox = new ComboBox<>();
        List<Group> groups = GroupsAPI.getAllGroups();
        ObservableList<String> groupNames = FXCollections.observableArrayList();
        for (Group group : groups) {
            groupNames.add(group.getName() + (group.isSpecialAccess() ? " (SAG)" : ""));
        }
        groupComboBox.setItems(groupNames);
        groupComboBox.setValue(null);

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Group:"), groupComboBox);
        dialog.getDialogPane().setContent(content);

        // Convert the result to group name when the assign button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                String selectedGroup = groupComboBox.getValue();
                if (selectedGroup == null) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a group.");
                    return null;
                }
                return null; // We handle the action separately
            }
            return null;
        });

        Optional<Void> result = dialog.showAndWait();
        result.ifPresent(v -> {
            String selectedGroup = groupComboBox.getValue();
            String groupName = selectedGroup.replace(" (SAG)", "").trim();
            boolean success = UsersAPI.addUserToGroup(username, groupName);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User assigned to group successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to assign user to group.");
            }
        });
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
