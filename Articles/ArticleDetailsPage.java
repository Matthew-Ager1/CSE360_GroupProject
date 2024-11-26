package Articles;

import java.util.concurrent.ExecutionException;
import Core.DataHolder;
import Core.Navigation;
import Database.ArticlesAPI;
import Database.Models.Article;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.HBox;

public class ArticleDetailsPage {
    public static void RegisterWithNavigation() {
        // Labels
        Label titleLabel = new Label("Article Details");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Article Details Labels
        Label idLabel = new Label();
        Label levelLabel = new Label();
        Label groupsLabel = new Label();
        Label sensitiveLabel = new Label();
        Label title = new Label();
        Label shortDescription = new Label();
        Label keywords = new Label();
        Text body = new Text();
        TextFlow bodyFlow = new TextFlow(body);
        Label links = new Label();
        Label nonSensitiveTitle = new Label();
        Label nonSensitiveDescription = new Label();

        // Feedback Section
        Label feedbackTitle = new Label("Add Feedback");
        feedbackTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Star Rating Section
        HBox starRating = new HBox(5);
        Rectangle[] stars = new Rectangle[5];
        for (int i = 0; i < 5; i++) {
            stars[i] = new Rectangle(30, 30);
            stars[i].setFill(Color.GREY);
            stars[i].setOnMouseClicked(e -> handleStarClick(e, stars));
            starRating.getChildren().add(stars[i]);
        }

        // Comments Box
        TextArea commentsBox = new TextArea();
        commentsBox.setPromptText("Add any additional comments here...");
        commentsBox.setPrefSize(300, 100);

        Button sendButton = new Button("Send");

        // Feedback Layout (Initially hidden)
        VBox feedbackLayout = new VBox(10);
        feedbackLayout.getChildren().addAll(feedbackTitle, starRating, commentsBox, sendButton);
        feedbackLayout.setVisible(false); // Hide initially

        sendButton.setOnAction(e -> {
            // Handle feedback submission (e.g., save feedback, hide form, etc.)
            System.out.println("Feedback Sent");

            // Hide feedback section after sending
            feedbackLayout.setVisible(false);
        });

        // Give Feedback Button
        Button giveFeedbackButton = new Button("Give Feedback");
        giveFeedbackButton.setOnAction(e -> {
            // Toggle the visibility of the feedback section
            boolean isVisible = feedbackLayout.isVisible();
            feedbackLayout.setVisible(!isVisible); // Show or hide feedback section
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> Navigation.navigateTo("ArticleSearchPage", null));

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getChildren().addAll(
                titleLabel,
                idLabel,
                levelLabel,
                groupsLabel,
                sensitiveLabel,
                new Label("Title:"), title,
                new Label("Short Description:"), shortDescription,
                new Label("Keywords:"), keywords,
                new Label("Body:"), bodyFlow,
                new Label("Links:"), links,
                new Label("Non-Sensitive Title:"), nonSensitiveTitle,
                new Label("Non-Sensitive Description:"), nonSensitiveDescription,
                giveFeedbackButton, // Add the Give Feedback button
                feedbackLayout,  // Add feedback section (which will be toggled)
                backButton
        );

        Scene scene = new Scene(layout, 600, 800);

        // Populate Data When Scene is Shown
        scene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
            if (newWindow != null) {
                long articleId = DataHolder.getInstance().getSelectedArticleId();
                Article article = null;
                try {
                    article = ArticlesAPI.getArticleAsync(articleId, "").get();
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
                if (article != null) {
                    idLabel.setText("ID: " + article.getId());
                    levelLabel.setText("Level: " + article.getLevel());
                    groupsLabel.setText("Groups: " + String.join(", ", article.getGroups()));
                    sensitiveLabel.setText("Sensitive: " + article.isSensitive());
                    title.setText(article.getTitle());
                    shortDescription.setText(article.getShortDescription());
                    keywords.setText(String.join(", ", article.getKeywords()));
                    body.setText(article.getBody());
                    links.setText(String.join(", ", article.getLinks()));
                    nonSensitiveTitle.setText(article.getNonSensitiveTitle());
                    nonSensitiveDescription.setText(article.getNonSensitiveDescription());
                } else {
                    title.setText("Article not found.");
                }
            }
        });

        Navigation.registerScene("ArticleDetailsPage", scene);
    }

    private static void handleStarClick(MouseEvent event, Rectangle[] stars) {
        // Handle filling stars based on user clicks
        for (int i = 0; i < 5; i++) {
            if (event.getSource() == stars[i]) {
                for (int j = 0; j < 5; j++) {
                    if (j <= i) {
                        stars[j].setFill(Color.YELLOW);
                    } else {
                        stars[j].setFill(Color.GREY);
                    }
                }
                break;
            }
        }
    }
}

