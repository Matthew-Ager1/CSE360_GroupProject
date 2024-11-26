package HomePage;

import Core.Navigation;
import Database.Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Articles.ArticleSearchPage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AdminHomePage {
    public static void RegisterWithNavigation(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Title Label
        Label titleLabel = new Label("Welcome " + user.getUsername() + "!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);

        // Topic Search Title
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
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                // Register the search query and navigate to ArticleSearchPage
                ArticleSearchPage.RegisterWithNavigation(query);
                Navigation.navigateTo("ArticleSearchPage", null);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a search query.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Admin Panel Button
        Button adminPanelButton = new Button("Admin Panel");
        adminPanelButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");
        adminPanelButton.setOnAction(event -> {
            Navigation.navigateTo("AdminPanel", user);
        });

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        logoutButton.setOnAction(event -> {
            Navigation.navigateTo("LoginPage", user);
        });

        // Layout for Main Buttons
        VBox mainButtons = new VBox(10);
        mainButtons.setPadding(new Insets(20));
        mainButtons.setAlignment(Pos.CENTER);
        mainButtons.getChildren().addAll(adminPanelButton, logoutButton);

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(new HBox(10, searchLabel, searchField, searchButton));
        mainLayout.setCenter(titleLabel);
        mainLayout.setBottom(mainButtons);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        // Scene Setup
        Scene scene = new Scene(mainLayout, 600, 400);
        Navigation.registerScene("AdminHomePage", scene);

        // Register search results page
        SearchResultPage.RegisterWithNavigation("Welcome");
    }
}


