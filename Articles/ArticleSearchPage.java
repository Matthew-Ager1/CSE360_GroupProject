package Articles;

import Core.DataHolder;
import Core.Navigation;
import Database.ArticlesAPI;
import Database.Models.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ArticleSearchPage {
    public static void RegisterWithNavigation() throws InterruptedException, ExecutionException {
        // Title Label
        Label titleLabel = new Label("Search Articles");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Enter article title");
        searchField.setMaxWidth(300);
        
        TableView<Article> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Search Button
        Button searchButton = new Button("Search");
        searchButton.setOnAction((ActionEvent event) -> {
            String query = searchField.getText().trim().toLowerCase();
            List<Article> allArticles = null;
			try {
				allArticles = ArticlesAPI.listArticlesAsync().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            List<Article> filtered = allArticles.stream()
                    .filter(article -> article.getTitle().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            tableView.setItems(FXCollections.observableArrayList(filtered));
        });

        // TableView Setup
        

        // Define Columns
        TableColumn<Article, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Article, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Article, String> shortDescColumn = new TableColumn<>("Short Description");
        shortDescColumn.setCellValueFactory(new PropertyValueFactory<>("shortDescription"));

        TableColumn<Article, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button detailsButton = new Button("View Details");

            {
                detailsButton.setOnAction((ActionEvent event) -> {
                    Article article = getTableView().getItems().get(getIndex());
                    DataHolder.getInstance().setSelectedArticleId(article.getId());
                    Navigation.navigateTo("ArticleDetailsPage");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                }
            }
        });

        tableView.getColumns().addAll(idColumn, titleColumn, shortDescColumn, actionColumn);

        // Initial Data
        List<Article> articles = ArticlesAPI.listArticlesAsync().get();
        ObservableList<Article> data = FXCollections.observableArrayList(articles);
        tableView.setItems(data);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> Navigation.navigateTo("ArticlesListPage"));

        // Layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, searchField, searchButton, tableView, backButton);

        Scene scene = new Scene(layout, 800, 600);

        Navigation.registerScene("ArticleSearchPage", scene);
    }
}
