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
import java.sql.SQLException;
import java.util.Scanner;

import HomePage.HomePageUI;
import application.Users;
import javafx.scene.layout.Pane;
import application.LoginPage.*;
import application.AccountCreation.*;

public class Main extends Application {
	
	private static final Users Users = new Users();
	public static void main(String[] args) {
		launch(args);
	}
	public void start(Stage baseStage) {
		Navigation.setPrimaryStage(baseStage);
		
		Users user = LoginPageUI.RegisterWithNavigation();
		
		HomePageUI.RegisterWithNavigation(user);
		
		
		
		
	    //AccountCreationUI.RegisterWithNavigation(baseStage);
		
        Navigation.navigateTo("LoginPage");
    }
}