package Core;

import javafx.application.Application;
import javafx.stage.Stage;
import LoginPage.LoginPage;
import HomePage.AdminPanel; // Make sure to import the AdminPanel

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Navigation.setPrimaryStage(primaryStage);

        // Register the login page
        LoginPage.RegisterWithNavigation();

        // Register the AdminPanel page
        AdminPanel.RegisterWithNavigation();

        // Set the initial scene to the login page
        Navigation.navigateTo("LoginPage");
    }
}
