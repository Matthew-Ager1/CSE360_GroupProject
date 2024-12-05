package HomePage;

import Core.Navigation;
import Database.Models.User;
import Database.Models.Article; // Import Article
import Database.UsersAPI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Arrays;

public class AdminPanel {
    public static void RegisterWithNavigation() {
        // Title Label
        Label titleLabel = new Label("Admin Panel");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // List Users Button
        Button listUsersButton = new Button("List Users");
        listUsersButton.setMaxWidth(Double.MAX_VALUE);
        listUsersButton.setOnAction(e -> {
            List<User> users = fetchUsersFromDatabase();
            ManageUsersPage.RegisterWithNavigation(users);  // Pass the list of users
            Navigation.navigateTo("ManageUsersPage", null);
        });

        // List Articles Button
        Button listArticlesButton = new Button("List Articles");
        listArticlesButton.setMaxWidth(Double.MAX_VALUE);
        listArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("ManageArticlesPage", null);
        });

        // Backup Articles Button
        Button backupArticlesButton = new Button("Backup Articles");
        backupArticlesButton.setMaxWidth(Double.MAX_VALUE);
        backupArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("BackupArticlesPage", null);
        });

        // Restore Articles Button
        Button restoreArticlesButton = new Button("Restore Articles");
        restoreArticlesButton.setMaxWidth(Double.MAX_VALUE);
        restoreArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("RestoreArticlesPage", null);
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white;");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> {
            Navigation.navigateTo("AdminHomePage", null);
        });

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setOnAction(e -> {
            Navigation.navigateTo("LoginPage", null);
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                titleLabel,
                listUsersButton,
                listArticlesButton,
                backupArticlesButton,
                restoreArticlesButton,
                backButton,  // Add back button here
                logoutButton
        );

        Scene scene = new Scene(layout, 400, 400);
        Navigation.registerScene("AdminPanel", scene);
    }

    private static List<User> fetchUsersFromDatabase() {
        return UsersAPI.getAllUsers();  // Fetch all users from the database
    }

    private static List<Article> fetchArticlesFromDatabase() {
        return Arrays.asList(
                new Article(1L, "Beginner", Arrays.asList("Group 1"), true, "Article 1", "Short Description 1", Arrays.asList("keyword1", "keyword2"), "Body of Article 1", Arrays.asList("link1"), "Non-sensitive Title 1", "Non-sensitive Description 1")
        );
    }

    private static void displayArticles(List<Article> articles) {
        articles.forEach(article -> System.out.println(article.getTitle() + " - " + article.getBody()));
    }
}

