package HomePage;

import Core.Navigation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminHomePage {
    public static void RegisterWithNavigation() {
        // Title Label
        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Manage Groups Button
        Button manageGroupsButton = new Button("Manage Groups");
        manageGroupsButton.setMaxWidth(Double.MAX_VALUE);
        manageGroupsButton.setOnAction(e -> {
            Navigation.navigateTo("ManageGroupsPage");
        });

        // Manage Users Button
        Button manageUsersButton = new Button("Manage Users");
        manageUsersButton.setMaxWidth(Double.MAX_VALUE);
        manageUsersButton.setOnAction(e -> {
            Navigation.navigateTo("ManageUsersPage");
        });

        // Manage Help Articles Button
        Button manageHelpArticlesButton = new Button("Manage Help Articles");
        manageHelpArticlesButton.setMaxWidth(Double.MAX_VALUE);
        manageHelpArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("ManageHelpArticlesPage");
        });

        // Backup Articles Button
        Button backupArticlesButton = new Button("Backup Articles");
        backupArticlesButton.setMaxWidth(Double.MAX_VALUE);
        backupArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("BackupArticlesPage");
        });

        // Restore Articles Button
        Button restoreArticlesButton = new Button("Restore Articles");
        restoreArticlesButton.setMaxWidth(Double.MAX_VALUE);
        restoreArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("RestoreArticlesPage");
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
                manageGroupsButton,
                manageUsersButton,
                manageHelpArticlesButton,
                backupArticlesButton,
                restoreArticlesButton,
                logoutButton
        );

        Scene scene = new Scene(layout, 400, 400);
        Navigation.registerScene("AdminHomePage", scene);
    }
}
