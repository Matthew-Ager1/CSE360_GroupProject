package HomePage;

import Core.Navigation;
import Database.GroupsAPI;
import Database.UsersAPI;
import Database.Models.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ManageStudentAccessPage {
    public static void RegisterWithNavigation() {
        // Title
        Label titleLabel = new Label("Manage Student Access");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ListView to display groups
        ListView<String> groupsListView = new ListView<>();
        refreshGroupsList(groupsListView);

        // Buttons
        Button addStudentButton = new Button("Add Student to Group");
        addStudentButton.setOnAction(e -> {
            String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
            if (selectedGroup != null) {
                String groupName = selectedGroup.replace(" (SAG)", "").trim();
                showAddStudentToGroupDialog(groupName);
                refreshGroupsList(groupsListView);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group.");
            }
        });

        Button removeStudentButton = new Button("Remove Student from Group");
        removeStudentButton.setOnAction(e -> {
            String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
            if (selectedGroup != null) {
                String groupName = selectedGroup.replace(" (SAG)", "").trim();
                showRemoveStudentFromGroupDialog(groupName);
                refreshGroupsList(groupsListView);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group.");
            }
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Navigation.navigateTo("InstructorHomePage");
        });

        // Layout for buttons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(addStudentButton, removeStudentButton, backButton);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, groupsListView, buttonsBox);

        Scene scene = new Scene(layout, 600, 500);
        Navigation.registerScene("ManageStudentAccessPage", scene);
    }

    private static void refreshGroupsList(ListView<String> groupsListView) {
        List<Group> groups = GroupsAPI.getAllGroups();
        ObservableList<String> groupNames = FXCollections.observableArrayList();
        for (Group group : groups) {
            groupNames.add(group.getName() + (group.isSpecialAccess() ? " (SAG)" : ""));
        }
        groupsListView.setItems(groupNames);
    }

    private static void showAddStudentToGroupDialog(String groupName) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Student to Group");
        dialog.setHeaderText("Enter student's username to add to group: " + groupName);

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the fields
        TextField studentUsernameField = new TextField();
        studentUsernameField.setPromptText("Student Username");

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Student Username:"), studentUsernameField);
        dialog.getDialogPane().setContent(content);

        // Convert the result to a username when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return studentUsernameField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(username -> {
            if (username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Username cannot be empty.");
                return;
            }
            boolean success = GroupsAPI.addUserToGroupRole(groupName, username, "students");
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student added to group successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to add student to group.");
            }
        });
    }

    private static void showRemoveStudentFromGroupDialog(String groupName) {
        // Retrieve students in the group
        Group group = GroupsAPI.getGroupByName(groupName);
        if (group == null || group.getStudents() == null || group.getStudents().isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Students", "There are no students in this group.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(group.getStudents().get(0), group.getStudents());
        dialog.setTitle("Remove Student from Group");
        dialog.setHeaderText("Select a student to remove from group: " + groupName);
        dialog.setContentText("Student Username:");

        dialog.showAndWait().ifPresent(username -> {
            boolean success = GroupsAPI.removeUserFromGroupRole(groupName, username, "students");
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student removed from group successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to remove student from group.");
            }
        });
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(type, message, ButtonType.OK);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }
}
