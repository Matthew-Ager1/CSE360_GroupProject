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

public class ArticlesListPage {
    public static void RegisterWithNavigation() throws InterruptedException, ExecutionException {
        // Title Label
        Label titleLabel = new Label("All Articles");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // TableView Setup
        TableView<Article> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // Corrected

        // Define Columns
        TableColumn<Article, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Article, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Article, String> shortDescColumn = new TableColumn<>("Short Description");
        shortDescColumn.setCellValueFactory(new PropertyValueFactory<>("shortDescription"));

        TableColumn<Article, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new TableCell<Article, Void>() { // Corrected
            private final Button detailsButton = new Button("View Details");

            {
                detailsButton.setOnAction((ActionEvent event) -> {
                    Article article = getTableView().getItems().get(getIndex());
                    DataHolder.getInstance().setSelectedArticleId(article.getId());
                    //ArticleDetailsPage.RegisterWithNavigation();
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

        // Populate Table
        List<Article> articles = ArticlesAPI.listArticlesAsync().get();
        ObservableList<Article> data = FXCollections.observableArrayList(articles);
        tableView.setItems(data);

        // Layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, tableView);

        Scene scene = new Scene(layout, 800, 600);

        Navigation.registerScene("ArticlesListPage", scene);
    }
}
