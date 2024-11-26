package HomePage;

import Core.Navigation;
import Database.GroupsAPI;
import Database.Models.Group;
import Database.Models.Role;
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
import java.util.Arrays;
import java.util.List;

public class ManageGroupsPage {
    public static void RegisterWithNavigation() {
        // Title
        Label titleLabel = new Label("Manage Groups");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ListView to display groups
        ListView<String> groupsListView = new ListView<>();
        refreshGroupsList(groupsListView);

        // Buttons
        Button createGroupButton = new Button("Create Group");
        createGroupButton.setOnAction(e -> {
            showCreateGroupDialog();
            refreshGroupsList(groupsListView);
        });

        Button editGroupButton = new Button("Edit Group");
        editGroupButton.setOnAction(e -> {
            String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
            if (selectedGroup != null) {
                showEditGroupDialog(selectedGroup);
                refreshGroupsList(groupsListView);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to edit.");
            }
        });

        Button deleteGroupButton = new Button("Delete Group");
        deleteGroupButton.setOnAction(e -> {
            String selectedGroup = groupsListView.getSelectionModel().getSelectedItem();
            if (selectedGroup != null) {
                boolean success = GroupsAPI.deleteGroup(selectedGroup);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Group deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failure", "Failed to delete group.");
                }
                refreshGroupsList(groupsListView);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a group to delete.");
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
        buttonsBox.getChildren().addAll(createGroupButton, editGroupButton, deleteGroupButton, backButton);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, groupsListView, buttonsBox);

        Scene scene = new Scene(layout, 500, 500);
        Navigation.registerScene("ManageGroupsPage", scene);
    }

    private static void refreshGroupsList(ListView<String> groupsListView) {
        List<Group> groups = GroupsAPI.getAllGroups();
        ObservableList<String> groupNames = FXCollections.observableArrayList();
        for (Group group : groups) {
            groupNames.add(group.getName() + (group.isSpecialAccess() ? " (SAG)" : ""));
        }
        groupsListView.setItems(groupNames);
    }

    private static void showCreateGroupDialog() {
        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Create New Group");
        dialog.setHeaderText("Enter group details:");

        // Set the button types.
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Create the fields
        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Group Name");

        CheckBox isSAGCheckBox = new CheckBox("Special Access Group");

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Group Name:"), groupNameField, isSAGCheckBox);
        dialog.getDialogPane().setContent(content);

        // Convert the result to a Group when the create button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                String name = groupNameField.getText();
                boolean isSAG = isSAGCheckBox.isSelected();
                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Group name cannot be empty.");
                    return null;
                }
                return new Group(name, isSAG, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(group -> {
            boolean success = GroupsAPI.createGroup(group);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group created successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to create group.");
            }
        });
    }

    private static void showEditGroupDialog(String groupNameWithFlag) {
        String groupName = groupNameWithFlag.replace(" (SAG)", "").trim();
        Group group = GroupsAPI.getGroupByName(groupName);
        if (group == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Group not found.");
            return;
        }

        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Edit Group");
        dialog.setHeaderText("Modify group details:");

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the fields
        TextField groupNameField = new TextField(group.getName());
        groupNameField.setEditable(false); // Group name should not be editable

        CheckBox isSAGCheckBox = new CheckBox("Special Access Group");
        isSAGCheckBox.setSelected(group.isSpecialAccess());

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Group Name:"), groupNameField, isSAGCheckBox);
        dialog.getDialogPane().setContent(content);

        // Convert the result to a Group when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                boolean isSAG = isSAGCheckBox.isSelected();
                group.setSpecialAccess(isSAG);
                return group;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedGroup -> {
            boolean success = GroupsAPI.updateGroup(updatedGroup);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Group updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to update group.");
            }
        });
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
