package HomePage;

import Core.Navigation;
import Database.Models.User;
import Database.Models.Article;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

public class AdminPanel {
    public static void RegisterWithNavigation() {
        Label titleLabel = new Label("Admin Panel");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button listUsersButton = new Button("List Users");
        listUsersButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        listUsersButton.setPadding(new Insets(10, 20, 10, 20));
        listUsersButton.setOnAction((ActionEvent event) -> {
            List<User> users = fetchUsersFromDatabase();
            UserManagementPage.RegisterWithNavigation(users);
            Navigation.navigateTo("UserManagementPage");
        });

        Button listArticlesButton = new Button("List Articles");
        listArticlesButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        listArticlesButton.setPadding(new Insets(10, 20, 10, 20));
        listArticlesButton.setOnAction((ActionEvent event) -> {
            List<Article> articles = fetchArticlesFromDatabase();
            displayArticles(articles);
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, listUsersButton, listArticlesButton);

        Scene scene = new Scene(layout, 350, 250);
        Navigation.registerScene("AdminPanel", scene);
    }

    private static List<User> fetchUsersFromDatabase() {
        // Placeholder for fetching users from database
        return Arrays.asList(
                new User("1", "Admin", "admin@example.com", "password", true, 1, Arrays.asList("admin", "instructor")),
                new User("2", "Instructor", "instructor@example.com", "password", true, 2, Arrays.asList("instructor"))
        );
    }

    private static List<Article> fetchArticlesFromDatabase() {
        // Placeholder for fetching articles from database
        return Arrays.asList(new Article(1L, "Beginner", Arrays.asList("Group 1"), true, "Article 1", "Short Description 1", Arrays.asList("keyword1", "keyword2"), "Body of Article 1", Arrays.asList("link1"), "Non-sensitive Title 1", "Non-sensitive Description 1"));
    }

    private static void displayArticles(List<Article> articles) {
        // Placeholder for displaying articles
        articles.forEach(article -> System.out.println(article.getTitle() + " - " + article.getBody()));
    }
}
