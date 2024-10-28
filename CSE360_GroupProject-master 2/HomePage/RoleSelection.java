package HomePage;

import Core.Navigation;
import Database.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RoleSelection {
	public static void RegisterWithNavigation(User user) {
		
		// For testing purposes, uncomment one of these lines
		// Default User
		// user.setUser("Team #62"); user.setPassword("Password") ; user.setRole(0);
		   
		// Instructor
		// user.setUser("Team #62"); user.setPassword("Password") ; user.setRole(1);
		   
		// Admin
		//user.setUser("Team #62"); user.setPassword("Password") ; user.setRole(2);
		
		
		
		
		// Role is selected via a dropdown
		ObservableList<String> options = FXCollections.observableArrayList();
		
		// Role Selection
		if (user.getPerms() == 0) {
			// Default
			UserHomePage.RegisterWithNavigation(user);
		} 
		else if (user.getPerms() == 1) {
			// Default
			// Instructor
			options.add("Default User");
			options.add("Instructor");
		}
		else if (user.getPerms() == 2) {
			// Default
			// Instructor
			// Admin
			options.add("Default User");
			options.add("Instructor");
			options.add("Admin");
		}
		
		// Title Label
		Label titleLabel = new Label("Please select an active role");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		
        // Dropdown
        final ComboBox comboBox = new ComboBox(options);
		comboBox.setValue("Default User");
        
		// Login Button
		Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setPadding(new Insets(10, 20, 10, 20));
        loginButton.setOnAction((ActionEvent event) -> {
        	// Send the user to the correct page
        	
        	// Default
        	if (comboBox.getValue() == "Default User") {
        		UserHomePage.RegisterWithNavigation(user);
        		Navigation.navigateTo("UserHome");
        	}
        	// Instructor
        	else if (comboBox.getValue() == "Instructor") {
        		InstructorHomePage.RegisterWithNavigation(user);
        		Navigation.navigateTo("InstructorHome");
        	}
        	// Admin
        	else {
        		AdminHomePage.RegisterWithNavigation(user);
        		Navigation.navigateTo("AdminHome");
        	}
        });
		
		
		VBox layout = new VBox(15);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(titleLabel, comboBox, loginButton);
		
		Scene scene = new Scene(layout, 350, 250);
		Navigation.registerScene("RoleSelection", scene);
		
	}
}
