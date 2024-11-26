package Core;

import javafx.application.Application;
import javafx.stage.Stage;
import Articles.ArticleDetailsPage;
import Articles.ArticleSearchPage;
import Articles.ArticlesListPage;
import HomePage.*;
import LoginPage.LoginPage;
import Database.UsersAPI;
import Database.Models.*;
import AccountCreation.*;
import java.util.concurrent.ExecutionException;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage baseStage) {

        Navigation.setPrimaryStage(baseStage);

        Navigation.clearAllScenes();

        LoginPage.RegisterWithNavigation();
        RegistrationPage.RegisterWithNavigation();

        List<User> users = UsersAPI.getAllUsers();
        ManageUsersPage.RegisterWithNavigation(users);

        try {
            ArticlesListPage.RegisterWithNavigation();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ArticleDetailsPage.RegisterWithNavigation();

        try {
            ArticleSearchPage.RegisterWithNavigation("Welcome");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ManageArticlesPage.RegisterWithNavigation();
        ManageGroupsPage.RegisterWithNavigation();
        AdminPanel.RegisterWithNavigation();

        User user = new User();

        AdminHomePage.RegisterWithNavigation(user);
        InstructorHomePage.RegisterWithNavigation(user);

        Navigation.navigateTo("LoginPage", null);
    }
}


