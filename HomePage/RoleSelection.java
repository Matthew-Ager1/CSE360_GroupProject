package HomePage;

import Database.Models.User;
import Core.Navigation;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import Database.Models.Role;


public class RoleSelection {

    public static void RegisterWithNavigation(User user) {
        Label titleLabel = new Label("Select Your Role");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Get the roles the user has access to
        List<String> roles = getAvailableRoles(user);

        // Create a ComboBox with the available roles
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(roles);
        roleComboBox.setPromptText("Choose a role");
        roleComboBox.setMaxWidth(200);

        // Display error message if no role is selected
        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Proceed button
        Button proceedButton = new Button("Proceed");
        proceedButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        proceedButton.setPadding(new Insets(10, 20, 10, 20));

        proceedButton.setOnAction((ActionEvent event) -> {
            String selectedRole = roleComboBox.getValue();

            if (selectedRole == null || selectedRole.isEmpty()) {
                errorMessageLabel.setText("Please select a role.");
            } else {
                // Proceed based on selected role
                switch (selectedRole) {
                    case "Admin":
                        Navigation.navigateTo("AdminHomePage", user);
                        break;
                    case "Instructor":
                        Navigation.navigateTo("InstructorHomePage", user);
                        break;
                    case "Student":
                        Navigation.navigateTo("StudentHomePage", user);
                        break;
                    default:
                        errorMessageLabel.setText("Invalid role selected.");
                        break;
                }
            }
        });

        VBox vbox = new VBox(10, titleLabel, roleComboBox, proceedButton, errorMessageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene roleSelectionScene = new Scene(vbox, 300, 250);
        Navigation.registerScene("RoleSelection", roleSelectionScene);
    }

    // This method returns the roles available for the user (Admin, Instructor, Student, etc.)
    private static List<String> getAvailableRoles(User user) {
        List<String> availableRoles = new ArrayList<>();

        // Get the user's roles
        Set<Role> roles = user.getRoles();

        // Check if the user has each role and add it to the list
        if (roles.contains(Role.ADMIN)) {
            availableRoles.add("Admin");
        }
        if (roles.contains(Role.INSTRUCTOR)) {
            availableRoles.add("Instructor");
        }
        if (roles.contains(Role.STUDENT)) {
            availableRoles.add("Student");
        }

        return availableRoles;
    }
}
