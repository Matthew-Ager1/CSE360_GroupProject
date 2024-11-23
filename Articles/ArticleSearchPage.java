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

    public static void RegisterWithNavigation(String query) {
        // Title Label
        Label titleLabel = new Label("Search Results for: " + query);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Search Field (pre-filled with the query, not editable)
        TextField searchField = new TextField(query);
        searchField.setPromptText("Enter article title");
        searchField.setMaxWidth(300);
        searchField.setDisable(false);

        // TableView for displaying articles
        TableView<Article> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // Use this instead of the deprecated one

        // TableView Columns
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
                detailsButton.setOnAction(event -> {
                    Article article = getTableView().getItems().get(getIndex());
                    DataHolder.getInstance().setSelectedArticleId(article.getId());
                    Navigation.navigateTo("ArticleDetailsPage");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : detailsButton);
            }
        });

        // Add the columns to the TableView
        tableView.getColumns().clear(); // Clear any pre-existing columns if present
        tableView.getColumns().addAll(idColumn, titleColumn, shortDescColumn, actionColumn);

        // Declare allArticles as final so it can be used inside lambda expressions
        final List<Article> allArticles;
        try {
            allArticles = ArticlesAPI.listArticlesAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return; // Exit if there is an error fetching articles
        }

        // Filter articles based on the initial query
        List<Article> filteredArticles = allArticles.stream()
                .filter(article -> article.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        tableView.setItems(FXCollections.observableArrayList(filteredArticles));

        // Search Button
        Button searchButton = new Button("Search");
        searchButton.setOnAction((ActionEvent event) -> {
            String updatedQuery = searchField.getText().trim();
            if (!updatedQuery.isEmpty()) {
                // Re-filter based on the updated search query
                List<Article> updatedFiltered = allArticles.stream()
                        .filter(article -> article.getTitle().toLowerCase().contains(updatedQuery.toLowerCase()))
                        .collect(Collectors.toList());
                tableView.setItems(FXCollections.observableArrayList(updatedFiltered));
            } else {
                // Show all articles if the search query is empty
                tableView.setItems(FXCollections.observableArrayList(allArticles));
            }
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> Navigation.navigateTo("AdminHomePage"));

        // Layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, searchField, searchButton, tableView, backButton);

        // Scene Setup
        Scene scene = new Scene(layout, 800, 600);
        Navigation.registerScene("ArticleSearchPage", scene);
    }
}

