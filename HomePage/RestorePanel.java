package HomePage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Core.Navigation;
import Database.ArticlesAPI;
import Database.Models.Article;
import Database.Models.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RestorePanel {
	public static void RegisterWithNavigation() {
		Label titleLabel = new Label("RESTORE");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);

        
        // POPULATING CHECK BOXES
        // Checkbox Pane
        VBox checkPane = new VBox();
        checkPane.setAlignment(Pos.CENTER);
        // List of articles & ids to backup
        List<Article> article_list = ArticlesAPI.listArticles();
        Set<Long> backup_targets = new HashSet<Long>();
        for (Article a : article_list) {
        	// Checkbox object
        	CheckBox c = new CheckBox("#: " + a.getId() + " | " + a.getTitle());
        	// Event handling
        	c.setOnAction((ActionEvent event) -> {
        		// Add to list of ids to backup
        		if (c.isSelected()) {
        			backup_targets.add(a.getId());
        		} 
        		// Remove from list of ids to backup
        		else {
        			backup_targets.remove(a.getId());
        		}
        		
        		/*
        		// Uncomment to show the selection
        		for (Long l : backup_targets) {
        			System.out.print(l + " ");
        		}
        		System.out.println();
        		*/
        		
        	});
        	
        	checkPane.getChildren().add(c);
        }
        
        
        
        // Backup Button
        Button backup_articles = new Button("Backup Article(s)");
        backup_articles.setOnAction((ActionEvent event) -> {
        	ArticlesAPI.backupArticles(backup_targets);
        });
        
        // Back Button
        Button back = new Button("Back");
        back.setOnAction((ActionEvent event) -> {
        	Navigation.navigateTo("AdminPanel");
        });
        
        
        VBox mainPanel = new VBox(10);
        mainPanel.setAlignment(Pos.CENTER);
        mainPanel.getChildren().addAll(checkPane, backup_articles, back);
        
      
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleLabel);
        mainLayout.setCenter(mainPanel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        
        Scene scene = new Scene(mainLayout, 600, 400);
        
        
        Navigation.registerScene("RestorePanel", scene);
	}
}