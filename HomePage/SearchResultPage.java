package HomePage;

import Core.Navigation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SearchResultPage {
	private static VBox feedbackLayout; // Declare feedbackLayout here

    public static void RegisterWithNavigation(String query) {
        // Result TextBox (with subtle border)
        Label resultText = new Label("Result goes here...");
        resultText.setPrefSize(500, 1000);  // Adjust size as needed
        resultText.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10px; -fx-border-color: #CCCCCC; -fx-border-width: 1px;");

        // Search Bar
        TextField searchField = new TextField(query);  // Pre-fill with previous search
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(250);

        // Search Button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String newQuery = searchField.getText();
            if (!newQuery.isEmpty()) {
                RegisterWithNavigation(newQuery); // Refresh the page with new search
                Navigation.navigateTo("SearchResultPage", null);
            }
        });

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");
        logoutButton.setOnAction(event -> {
            Navigation.navigateTo("LoginPage", null);
        });

        // Feedback Section (5-star rating and comments)
        Button feedbackButton = new Button("Add Feedback");

        // Star rating system (5 clickable stars)
        HBox starRating = new HBox(5);
        Rectangle[] stars = new Rectangle[5];
        for (int i = 0; i < 5; i++) {
            stars[i] = new Rectangle(30, 30);
            stars[i].setFill(Color.GREY);
            stars[i].setOnMouseClicked(e -> handleStarClick(e, stars));
            starRating.getChildren().add(stars[i]);
        }

        // TextArea for additional comments
        TextArea commentsBox = new TextArea();
        commentsBox.setPromptText("Add any additional comments here...");
        commentsBox.setPrefSize(300, 100);

        // Send button to submit feedback
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            feedbackLayout.setVisible(false); // Hide feedback section on send
        });

        // Feedback layout (star rating, comments, and send button)
        feedbackLayout = new VBox(10); // Initialize feedbackLayout
        feedbackLayout.getChildren().addAll(starRating, commentsBox, sendButton);
        feedbackLayout.setVisible(false);  // Initially hidden
        feedbackLayout.setStyle("-fx-padding: 5px;");

        // Show feedback form when button is clicked
        feedbackButton.setOnAction(e -> feedbackLayout.setVisible(true));

        // Top Bar Layout (Search Bar, Search Button, Logout Button)
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.getChildren().addAll(searchField, searchButton, logoutButton);

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);

        // Center Layout: Includes result display and feedback button/form
        VBox centerLayout = new VBox(10);
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.getChildren().addAll(resultText, feedbackButton, feedbackLayout);
        mainLayout.setCenter(centerLayout);
        BorderPane.setAlignment(centerLayout, Pos.CENTER);

        // Scene Setup
        Scene scene = new Scene(mainLayout, 600, 400);
        Navigation.registerScene("SearchResultPage", scene);
    }

    private static void handleStarClick(MouseEvent e, Rectangle[] stars) {
        Rectangle clickedStar = (Rectangle) e.getSource();
        int clickedIndex = -1;

        // Find the clicked star's index
        for (int i = 0; i < stars.length; i++) {
            if (stars[i] == clickedStar) {
                clickedIndex = i;
                break;
            }
        }

        // Set color for stars based on the clicked index
        for (int i = 0; i < stars.length; i++) {
            if (i <= clickedIndex) {
                stars[i].setFill(Color.GOLD);  // Filled star
            } else {
                stars[i].setFill(Color.GREY);  // Empty star
            }
        }
    }
}
