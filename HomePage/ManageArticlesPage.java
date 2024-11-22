package HomePage;

import Core.Navigation;
import Database.ArticlesAPI;
import Database.Models.Article;
import Database.GroupsAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ManageArticlesPage {
    public static void RegisterWithNavigation() {
        // Title Label
        Label titleLabel = new Label("Manage Articles");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // TableView to display articles
        TableView<Article> tableView = new TableView<>();
        setupArticlesTable(tableView);

        // Buttons
        Button addArticleButton = new Button("Add Article");
        addArticleButton.setOnAction(e -> {
            showAddArticleDialog();
            loadArticlesAsync(tableView);
        });

        Button editArticleButton = new Button("Edit Article");
        editArticleButton.setOnAction(e -> {
            Article selectedArticle = tableView.getSelectionModel().getSelectedItem();
            if (selectedArticle != null) {
                showEditArticleDialog(selectedArticle);
                loadArticlesAsync(tableView);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to edit.");
            }
        });

        Button deleteArticleButton = new Button("Delete Article");
        deleteArticleButton.setOnAction(e -> {
            Article selectedArticle = tableView.getSelectionModel().getSelectedItem();
            if (selectedArticle != null) {
                boolean confirmed = showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete article: " + selectedArticle.getTitle() + "?");
                if (confirmed) {
                    ArticlesAPI.deleteArticleAsync(selectedArticle.getId()).thenAccept(success -> {
                        if (success) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Article deleted successfully.");
                            loadArticlesAsync(tableView);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Failure", "Failed to delete article.");
                        }
                    });
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to delete.");
            }
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Navigation.goBack();  // This will go back to the previous page based on history
        });

        // Layout for buttons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(addArticleButton, editArticleButton, deleteArticleButton, backButton);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, tableView, buttonsBox);

        Scene scene = new Scene(layout, 800, 600);
        Navigation.registerScene("ManageArticlesPage", scene);

        // Load Articles
        loadArticlesAsync(tableView);
    }

    private static void setupArticlesTable(TableView<Article> tableView) {
        TableColumn<Article, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Article, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Article, String> shortDescColumn = new TableColumn<>("Short Description");
        shortDescColumn.setCellValueFactory(new PropertyValueFactory<>("shortDescription"));

        TableColumn<Article, String> groupsColumn = new TableColumn<>("Groups");
        groupsColumn.setCellValueFactory(new PropertyValueFactory<>("groups"));

        tableView.getColumns().addAll(idColumn, titleColumn, shortDescColumn, groupsColumn);
    }

    private static void loadArticlesAsync(TableView<Article> tableView) {
        ArticlesAPI.listArticlesAsync().thenAccept(articles -> {
            ObservableList<Article> data = FXCollections.observableArrayList(articles);
            javafx.application.Platform.runLater(() -> {
                tableView.setItems(data);
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load articles.");
            });
            return null;
        });
    }

    private static void showAddArticleDialog() {
        Dialog<Article> dialog = new Dialog<>();
        dialog.setTitle("Add New Article");
        dialog.setHeaderText("Enter article details:");

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the fields
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextArea shortDescArea = new TextArea();
        shortDescArea.setPromptText("Short Description");
        shortDescArea.setPrefRowCount(3);

        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Body");
        bodyArea.setPrefRowCount(10);

        TextField keywordsField = new TextField();
        keywordsField.setPromptText("Keywords (comma-separated)");

        TextField groupsField = new TextField();
        groupsField.setPromptText("Groups (comma-separated)");

        CheckBox sensitiveCheckBox = new CheckBox("Sensitive");

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Short Description:"), shortDescArea,
                new Label("Body:"), bodyArea,
                new Label("Keywords:"), keywordsField,
                new Label("Groups:"), groupsField,
                sensitiveCheckBox
        );
        dialog.getDialogPane().setContent(content);

        // Convert the result to an Article when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = titleField.getText();
                String shortDesc = shortDescArea.getText();
                String body = bodyArea.getText();
                List<String> keywords = Arrays.asList(keywordsField.getText().split(","));
                List<String> groups = Arrays.asList(groupsField.getText().split(","));
                boolean sensitive = sensitiveCheckBox.isSelected();

                if (title.isEmpty() || shortDesc.isEmpty() || body.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Title, Short Description, and Body are required.");
                    return null;
                }

                Article article = new Article();
                article.setTitle(title);
                article.setShortDescription(shortDesc);
                article.setBody(body);
                article.setKeywords(keywords);
                article.setGroups(groups);
                article.setSensitive(sensitive);
                // Set other fields as necessary

                return article;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(article -> {
            ArticlesAPI.addArticleAsync(article).thenAccept(success -> {
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Article added successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failure", "Failed to add article.");
                }
            });
        });
    }

    private static void showEditArticleDialog(Article article) {
        Dialog<Article> dialog = new Dialog<>();
        dialog.setTitle("Edit Article");
        dialog.setHeaderText("Modify article details:");

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the fields with existing data
        TextField titleField = new TextField(article.getTitle());
        TextArea shortDescArea = new TextArea(article.getShortDescription());
        shortDescArea.setPrefRowCount(3);
        TextArea bodyArea = new TextArea(article.getBody());
        bodyArea.setPrefRowCount(10);
        TextField keywordsField = new TextField(String.join(",", article.getKeywords()));
        TextField groupsField = new TextField(String.join(",", article.getGroups()));
        CheckBox sensitiveCheckBox = new CheckBox("Sensitive");
        sensitiveCheckBox.setSelected(article.isSensitive());

        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Short Description:"), shortDescArea,
                new Label("Body:"), bodyArea,
                new Label("Keywords:"), keywordsField,
                new Label("Groups:"), groupsField,
                sensitiveCheckBox
        );
        dialog.getDialogPane().setContent(content);

        // Convert the result to an Article when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String title = titleField.getText();
                String shortDesc = shortDescArea.getText();
                String body = bodyArea.getText();
                List<String> keywords = Arrays.asList(keywordsField.getText().split(","));
                List<String> groups = Arrays.asList(groupsField.getText().split(","));
                boolean sensitive = sensitiveCheckBox.isSelected();

                if (title.isEmpty() || shortDesc.isEmpty() || body.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Title, Short Description, and Body are required.");
                    return null;
                }

                article.setTitle(title);
                article.setShortDescription(shortDesc);
                article.setBody(body);
                article.setKeywords(keywords);
                article.setGroups(groups);
                article.setSensitive(sensitive);

                return article;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedArticle -> {
            ArticlesAPI.updateArticleAsync(updatedArticle).thenAccept(success -> {
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Article updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failure", "Failed to update article.");
                }
            });
        });
    }

    private static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(type, message, ButtonType.OK);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }
}
