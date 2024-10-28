package HomePage;

import Core.Navigation;
import Database.Models.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class AdminHomePage {
	public static void RegisterWithNavigation(User user) {
		
        // Title Label
        Label titleLabel = new Label("Welcome Admin " + user.getName() + "!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);
        
        // Topic Seach Title
        Label searchLabel = new Label("Search Topic: ");
        searchLabel.setStyle("-fx-font-size: 24px;");
        searchLabel.setAlignment(Pos.CENTER);

        // Topic Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(250);
        
        // Admin Panel (WIP)
        Button adminPanel = new Button("Admin Panel");
        adminPanel.setStyle("-fx-background-color: #ff3232;  -fx-text-fill: white;");
        adminPanel.setPadding(new Insets(10, 20, 10, 20));
        adminPanel.setOnAction((ActionEvent event) -> {
            // Open Admin Panel
        	System.out.println("OPEN ADMIN PANEL");
        });

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        logoutButton.setOnAction(event -> {
            // TODO: Add actual logout logic here
            Navigation.navigateTo("LoginPage");
        });

        // Top Bar Layout (Search Bar and Logout Button)
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.getChildren().addAll(searchLabel, searchField, adminPanel, logoutButton);

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        // Scene Setup
        Scene scene = new Scene(mainLayout, 600, 400);
        
        Navigation.registerScene("AdminHome", scene);
	}
}
