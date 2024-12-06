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

public class AdminPanel {
	public static void RegisterWithNavigation() {
		Label titleLabel = new Label("ADMIN PANEL");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);
        
        BackupPanel.RegisterWithNavigation();
        
        
        
        Button edit = new Button("Edit Article");
        edit.setOnAction((ActionEvent event) -> {
        	// Edit Article
        });
        
        Button delete = new Button("Delete Article");
        delete.setOnAction((ActionEvent event) -> {
        	// Delete Article
        });
        
        Button backup = new Button("Backup Article(s)");
        backup.setOnAction((ActionEvent event) -> {
        	// Backup Articles
        	Navigation.navigateTo("BackupPanel");
        });
        
        Button restore = new Button("Restore Article(s)");
        restore.setOnAction((ActionEvent event) -> {
        	// Restore Articles
        	Navigation.navigateTo("RestorePanel");
        });
        
        
        Button exit = new Button("Exit");
        exit.setOnAction((ActionEvent event) -> {
        	Navigation.navigateTo("AdminHome");
        });
        
        
        
        HBox mainPanel = new HBox(10);
        mainPanel.setAlignment(Pos.CENTER);
        mainPanel.getChildren().addAll(edit, delete, backup, restore, exit);
        
        
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleLabel);
        mainLayout.setCenter(mainPanel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        
        Scene scene = new Scene(mainLayout, 600, 400);
        
        Navigation.registerScene("AdminPanel", scene);
	}
}