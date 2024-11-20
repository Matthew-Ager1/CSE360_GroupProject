package HomePage;

import Core.Navigation;
import Database.ArticlesAPI;
import Database.GroupsAPI;
import Database.Models.Role;
import Database.Models.Group;
import Database.Models.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class InstructorHomePage {
    public static void RegisterWithNavigation() {
        // Title Label
        Label titleLabel = new Label("Instructor Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Manage Articles Button
        Button manageArticlesButton = new Button("Manage Articles");
        manageArticlesButton.setMaxWidth(Double.MAX_VALUE);
        manageArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("ManageArticlesPage");
        });

        // Backup/Restore Groups Button
        Button backupRestoreGroupsButton = new Button("Backup/Restore Groups");
        backupRestoreGroupsButton.setMaxWidth(Double.MAX_VALUE);
        backupRestoreGroupsButton.setOnAction(e -> {
            Navigation.navigateTo("BackupRestoreGroupsPage");
        });

        // Manage Student Access Button
        Button manageStudentAccessButton = new Button("Manage Student Access");
        manageStudentAccessButton.setMaxWidth(Double.MAX_VALUE);
        manageStudentAccessButton.setOnAction(e -> {
            Navigation.navigateTo("ManageStudentAccessPage");
        });

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setOnAction(e -> {
            Navigation.navigateTo("LoginPage");
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                titleLabel,
                manageArticlesButton,
                backupRestoreGroupsButton,
                manageStudentAccessButton,
                logoutButton
        );

        Scene scene = new Scene(layout, 400, 400);
        Navigation.registerScene("InstructorHomePage", scene);
    }
}
