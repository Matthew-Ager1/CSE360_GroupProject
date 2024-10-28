package Articles;

import Core.Navigation;
import Database.Models.Article;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class List_of_articles {
	public static void ArticleListWithNavigation(User user) {
		Label titleLabel = new Label("Select weather to display all articles or certain articles group/s:");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		
		Button DisplayAll = new Button("Display all");
		DisplayAll.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
		DisplayAll.setPadding(new Insets(10, 20, 10, 20));
		DisplayAll.setOnAction(event -> {
			listArticles();
		});
		TextField groupField = new TextField();
        groupField.setPromptText("Enter the groups you want to list:");
        Button Displaygroups = new Button("Display groups");
        Displaygroups.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        Displaygroups.setPadding(new Insets(10, 20, 10, 20));
        Displaygroups.setOnAction(event -> {
        	listArticlesByGroups(groupField);
		});

		
		VBox layout = new VBox(15);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(titleLabel, DisplayAll, loginButton);
	}
}