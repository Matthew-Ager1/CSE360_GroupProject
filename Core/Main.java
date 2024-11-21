package Core;

import javafx.application.Application;
import javafx.stage.Stage;
import Articles.ArticleDetailsPage;
import Articles.ArticleSearchPage;
import Articles.ArticlesListPage;
import HomePage.*;
import LoginPage.LoginPage;
import Database.Models.User;
import Database.Models.Role;
import Database.UsersAPI; // Import UsersAPI
import java.util.concurrent.ExecutionException;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage baseStage) {
        Navigation.setPrimaryStage(baseStage);

        // Register pages
        LoginPage.RegisterWithNavigation();

        // Placeholder user for development/testing
        User placeholderUser = new User();
        placeholderUser.setUsername("placeholder");
        placeholderUser.setName("Placeholder User");
        placeholderUser.setRole(Role.ADMIN);

        // Register StudentHomePage and AdminHomePage with a user object
        StudentHomePage.RegisterWithNavigation(placeholderUser);
        AdminHomePage.RegisterWithNavigation(placeholderUser); // Pass the user object

        // Fetch users from the database
        List<User> users = UsersAPI.getAllUsers();
        ManageUsersPage.RegisterWithNavigation(users); // Pass the list of users

        // Other page registrations...
        try {
            ArticlesListPage.RegisterWithNavigation();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ArticleDetailsPage.RegisterWithNavigation();
        try {
            ArticleSearchPage.RegisterWithNavigation();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ManageArticlesPage.RegisterWithNavigation();
        ManageGroupsPage.RegisterWithNavigation();
        InstructorHomePage.RegisterWithNavigation();
        AdminPanel.RegisterWithNavigation();

        Navigation.navigateTo("LoginPage"); // Open to LoginPage first
    }
}

