package Core;

import javafx.application.Application;
import javafx.stage.Stage;
import Articles.ArticleDetailsPage;
import Articles.ArticleSearchPage;
import Articles.ArticlesListPage;
import HomePage.*;
import LoginPage.LoginPage;
import Database.UsersAPI; // Import UsersAPI
import java.util.concurrent.ExecutionException;
import java.util.List;
import Database.Models.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage baseStage) {
        Navigation.setPrimaryStage(baseStage);

        // Register pages
        LoginPage.RegisterWithNavigation();

        // Fetch users from the database
        List<User> users = UsersAPI.getAllUsers();
        ManageUsersPage.RegisterWithNavigation(users); // Pass the list of users

        // Register other pages...
        try {
            ArticlesListPage.RegisterWithNavigation();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ArticleDetailsPage.RegisterWithNavigation();
        try {
            ArticleSearchPage.RegisterWithNavigation("Welcome");
        } catch (Exception e) { // Catch only relevant exceptions or remove the block if unnecessary
            e.printStackTrace();
        }


        ManageArticlesPage.RegisterWithNavigation();
        ManageGroupsPage.RegisterWithNavigation();
        InstructorHomePage.RegisterWithNavigation(null); // Pass null for now, user will be passed on login
        AdminPanel.RegisterWithNavigation();

        // Navigate to the LoginPage first
        Navigation.navigateTo("LoginPage");
    }
}


