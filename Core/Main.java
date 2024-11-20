package Core;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import Articles.ArticleDetailsPage;
import Articles.ArticleSearchPage;
import Articles.ArticlesListPage;
import HomePage.*;
import LoginPage.LoginPage;
import javafx.scene.layout.Pane;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	public void start(Stage baseStage) {
		Navigation.setPrimaryStage(baseStage);
		
		LoginPage.RegisterWithNavigation();
		
		//UserHomePage.RegisterWithNavigation();
		//InstructorHomePage.RegisterWithNavigation();
		
		
		try {
			ArticlesListPage.RegisterWithNavigation();
		} catch (InterruptedException e) {

		} catch (ExecutionException e) {

		}
        ArticleDetailsPage.RegisterWithNavigation();
        try {
			ArticleSearchPage.RegisterWithNavigation();
		} catch (InterruptedException e) {

		} catch (ExecutionException e) {

		}
        
        ManageArticlesPage.RegisterWithNavigation();
        ManageGroupsPage.RegisterWithNavigation();
        ManageUsersPage.RegisterWithNavigation();
        AdminHomePage.RegisterWithNavigation();
        InstructorHomePage.RegisterWithNavigation();
        
		
		
	    //AccountCreationUI.RegisterWithNavigation(baseStage);
		
        Navigation.navigateTo("ManageArticlesPage");
    }
}