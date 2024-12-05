package HomePage;

import Core.Navigation;
import Database.ArticlesAPI;
import Database.GroupsAPI;
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
import java.util.Optional;

public class BackupRestoreGroupsPage {
    public static void RegisterWithNavigation() {
        // Title Label
        Label titleLabel = new Label("Backup/Restore Groups");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ListView to display groups
        ListView<String> groupsListView = new ListView<>();
        refreshGroupsList(groupsListView);

        // Backup Button
        Button backupButton = new Button("Backup Selected Groups");
        backupButton.setOnAction(e -> {
            List<String> selectedGroups = groupsListView.getSelectionModel().getSelectedItems();
            if (selectedGroups.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select at least one group to backup.");
                return;
            }
            // Prompt for filename
            TextInputDialog dialog = new TextInputDialog("backup.json");
            dialog.setTitle("Backup Groups");
            dialog.setHeaderText("Enter filename for backup:");
            dialog.setContentText("Filename:");

            dialog.showAndWait().ifPresent(filename -> {
                List<String> groupNames = new ArrayList<>();
                for (String group : selectedGroups) {
                    groupNames.add(group.replace(" (SAG)", "").trim());
                }
                ArticlesAPI.backupArticlesAsync(filename, groupNames).thenAccept(success -> {
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Backup completed successfully.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failure", "Backup failed.");
                    }
                });
            });
        });

        // Restore Button
        Button restoreButton = new Button("Restore Groups from Backup");
        restoreButton.setOnAction(e -> {
            // Prompt for filename
            TextInputDialog dialog = new TextInputDialog("backup.json");
            dialog.setTitle("Restore Groups");
            dialog.setHeaderText("Enter filename to restore from:");
            dialog.setContentText("Filename:");

            dialog.showAndWait().ifPresent(filename -> {
                // Optionally, allow selecting specific groups to restore
                ChoiceDialog<String> groupChoiceDialog = new ChoiceDialog<>("All Groups", "All Groups", "Specific Groups");
                groupChoiceDialog.setTitle("Restore Option");
                groupChoiceDialog.setHeaderText("Choose restore option:");
                groupChoiceDialog.setContentText("Option:");

                groupChoiceDialog.showAndWait().ifPresent(option -> {
                    List<String> groupsToRestore = new ArrayList<>();
                    if (option.equals("All Groups")) {
                        groupsToRestore = null; // BackupArticlesAPI handles null as all groups
                    } else if (option.equals("Specific Groups")) {
                        // Let user select specific groups
                        List<Group> allGroups = GroupsAPI.getAllGroups();
                        List<String> groupNames = new ArrayList<>();
                        for (Group group : allGroups) {
                            groupNames.add(group.getName() + (group.isSpecialAccess() ? " (SAG)" : ""));
                        }
                        List<String> selectedGroups = showMultipleSelectionDialog("Select Groups", "Choose groups to restore:", groupNames);
                        if (selectedGroups != null && !selectedGroups.isEmpty()) {
                            for (String group : selectedGroups) {
                                groupsToRestore.add(group.replace(" (SAG)", "").trim());
                            }
                        } else {
                            showAlert(Alert.AlertType.WARNING, "No Selection", "No groups selected for restoration.");
                            return;
                        }
                    }

                    boolean removeExisting = showConfirmationDialog("Remove Existing", "Do you want to remove existing articles before restoring?");
                    ArticlesAPI.restoreArticlesAsync(filename, groupsToRestore, removeExisting).thenAccept(success -> {
                        if (success) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Restore completed successfully.");
                            refreshGroupsList(groupsListView);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Failure", "Restore failed.");
                        }
                    });
                });
            });
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Navigation.navigateTo("InstructorHomePage", null);
        });

        // Layout for buttons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(backupButton, restoreButton, backButton);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, groupsListView, buttonsBox);

        Scene scene = new Scene(layout, 600, 500);
        Navigation.registerScene("BackupRestoreGroupsPage", scene);
    }

    private static void refreshGroupsList(ListView<String> groupsListView) {
        List<Group> groups = GroupsAPI.getAllGroups();
        ObservableList<String> groupNames = FXCollections.observableArrayList();
        for (Group group : groups) {
            groupNames.add(group.getName() + (group.isSpecialAccess() ? " (SAG)" : ""));
        }
        groupsListView.setItems(groupNames);
        groupsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private static List<String> showMultipleSelectionDialog(String title, String header, List<String> options) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        // Set the button types.
        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);

        // Create the ListView
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(options));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        dialog.getDialogPane().setContent(listView);

        // Convert the result to a list of selected items when the select button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return listView.getSelectionModel().getSelectedItems();
            }
            return null;
        });

        Optional<List<String>> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(type, message, ButtonType.OK);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }
}
