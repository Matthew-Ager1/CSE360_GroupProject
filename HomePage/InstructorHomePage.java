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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import Database.Models.User;  // Import User class

public class InstructorHomePage {
    // Modify the method to accept a User object
    public static void RegisterWithNavigation(User user) {
        // Check if the user is null before proceeding
        if (user == null) {
            System.out.println("Error: User is null.");
            return; // Optionally, return early or handle this scenario appropriately
        }

        // Title Label
        Label titleLabel = new Label("Instructor Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Optional: Display username if desired
        Label usernameLabel = new Label("Welcome, " + user.getUsername());
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");
        
        Label searchLabel = new Label("Search Topic: ");
        searchLabel.setStyle("-fx-font-size: 24px;");
        searchLabel.setAlignment(Pos.CENTER);

        // Topic Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(250);

        // Search Button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                SearchResultPage.RegisterWithNavigation(query);
                Navigation.navigateTo("ArticleSearchPage", null);
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a search query.", ButtonType.OK);
                alert.showAndWait();
            }
        });
        // Manage Articles Button
        Button manageArticlesButton = new Button("Manage Articles");
        manageArticlesButton.setMaxWidth(Double.MAX_VALUE);
        manageArticlesButton.setOnAction(e -> {
            Navigation.navigateTo("ManageArticlesPage", null);
        });

        // Backup/Restore Groups Button
        Button backupRestoreGroupsButton = new Button("Backup/Restore Groups");
        backupRestoreGroupsButton.setMaxWidth(Double.MAX_VALUE);
        backupRestoreGroupsButton.setOnAction(e -> {
            Navigation.navigateTo("BackupRestoreGroupsPage", null);
        });

        // Manage Student Access Button
        Button manageStudentAccessButton = new Button("Manage Student Access");
        manageStudentAccessButton.setMaxWidth(Double.MAX_VALUE);
        manageStudentAccessButton.setOnAction(e -> {
            Navigation.navigateTo("ManageStudentAccessPage", null);
        });

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setOnAction(e -> {
            Navigation.navigateTo("LoginPage", null);
        });

        // Layout
        VBox layout1 = new VBox(10);
        layout1.setPadding(new Insets(20));
        layout1.setAlignment(Pos.CENTER);
        layout1.getChildren().addAll(
        		titleLabel,
                usernameLabel  // Optionally show username
                
        );
        
        VBox layout2 = new VBox(10);
        layout2.setPadding(new Insets(20));
        layout2.setAlignment(Pos.CENTER);
        layout2.getChildren().addAll(
                
                manageArticlesButton,
                backupRestoreGroupsButton,
                manageStudentAccessButton,
                logoutButton
        );
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(new HBox(10, searchLabel, searchField, searchButton));
        mainLayout.setCenter( layout1);
        mainLayout.setBottom(layout2);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        Scene scene = new Scene(mainLayout, 600, 400);
        Navigation.registerScene("InstructorHomePage", scene);
    }
}

