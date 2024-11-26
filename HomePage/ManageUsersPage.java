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
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.time.LocalDateTime;

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
            showAddUserDialog(users);
            refreshUsersList(usersListView, users);
        });

        Button changeRolesButton = new Button("Change Roles");
        changeRolesButton.setOnAction(e -> {
            String selectedUser = usersListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String username = selectedUser.split(" - ")[0];
                showChangeRolesDialog(username, users);
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
            Navigation.navigateTo("AdminHomePage", null);
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

        // Filter out any null users from the list
        List<User> validUsers = new ArrayList<>();
        for (User user : users) {
            if (user != null && user.getUsername() != null && !user.getUsername().isEmpty()) {
                validUsers.add(user);
            }
        }

        // Set up custom ListCell renderer
        usersListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Extract the username and the user's isFinished status
                    String username = item.split(" - ")[0];
                    User user = UsersAPI.getUserByUsername(username);  // Fetch user object using the username
                    HBox userBox = new HBox(5);
                    Text userText = new Text(item);
                    userBox.getChildren().add(userText);

                    // Check if the user is verified or not
                    Text verificationText = new Text();
                    if (user != null) {
                        if (user.getIsFinished()) {
                            // If user is verified, show a bold blue "V"
                            verificationText.setText(" V");
                            verificationText.setFont(Font.font("Arial", 16));
                            verificationText.setFill(Color.BLUE);
                            verificationText.setStyle("-fx-font-weight: bold;");
                        } else {
                            // If user is not verified, show grey "NV"
                            verificationText.setText(" NV");
                            verificationText.setFont(Font.font("Arial", 16));
                            verificationText.setFill(Color.GRAY);  // Grey color for unverified
                        }
                    }

                    userBox.getChildren().add(verificationText);  // Add "V" or "NV" to the HBox
                    setGraphic(userBox);  // Set the whole cell's graphic as the HBox containing the user and verification text
                }
            }
        });

        // Populate the ListView with the filtered list of valid users
        for (User user : validUsers) {
            userStrings.add(user.getUsername() + " - " + user.getRoles());
        }

        usersListView.setItems(userStrings);
    }

    private static void showAddUserDialog(List<User> users) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter user details:");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create fields for user input
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Middle Name (Optional)");

        CheckBox onePassCheckBox = new CheckBox("OnePass");
        onePassCheckBox.setSelected(false);

        TextField expireField = new TextField();
        expireField.setPromptText("Expiration (in days)");

        ComboBox<Role> roleComboBox = new ComboBox<>();
        roleComboBox.setItems(FXCollections.observableArrayList(Role.ADMIN, Role.INSTRUCTOR, Role.STUDENT));
        roleComboBox.setValue(Role.STUDENT);

        // Layout for input fields
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Email:"), emailField,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("First Name:"), firstNameField,
                new Label("Last Name:"), lastNameField,
                new Label("Middle Name:"), middleNameField,
                onePassCheckBox,
                new Label("Expiration:"), expireField,
                new Label("Role:"), roleComboBox
        );
        dialog.getDialogPane().setContent(content);

        // Convert the result to a User object when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String email = emailField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String middleName = middleNameField.getText();
                boolean onePass = onePassCheckBox.isSelected();
                int expire = Integer.parseInt(expireField.getText());
                Role role = roleComboBox.getValue();

                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "All fields must be filled.");
                    return null;
                }

                // Create role set and groups list
                Set<Role> roles = Set.of(role); // Assuming one role for simplicity
                List<String> groups = List.of(); // Placeholder for empty groups
                LocalDateTime passwordExpirationDate = LocalDateTime.now().plusDays(expire);  // Set expiration date based on input
                Boolean isFinished = false; // Placeholder for finished status

                // Construct a new User object with the updated constructor
                return new User(email, username, firstName, lastName, middleName, onePass, expire, roles, groups,
                        middleName.isEmpty() ? null : middleName, passwordExpirationDate, isFinished);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            boolean success = UsersAPI.addUser(user);
            if (success) {
                users.add(user);  // Add the new user to the list
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to add user.");
            }
        });
    }

    private static void showChangeRolesDialog(String username, List<User> users) {
        User user = UsersAPI.getUserByUsername(username);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "User Not Found", "The selected user could not be found.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Change Roles");
        dialog.setHeaderText("Select roles for user: " + user.getUsername());

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
                    user.setRoles(newRoles);  // Update the roles in the user object
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

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
