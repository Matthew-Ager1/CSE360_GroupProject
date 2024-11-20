package HomePage;

import Core.Navigation;
import Database.Models.User;
import LoginPage.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class RoleSelection {
    public static void RegisterWithNavigation(User user) {
        Label titleLabel = new Label("Please select your roles");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        CheckBox adminCheckBox = new CheckBox("Admin");
        CheckBox instructorCheckBox = new CheckBox("Instructor");
        CheckBox studentCheckBox = new CheckBox("Student");

        // Set selected based on current roles
        if (user.getRoles().contains("admin")) {
            adminCheckBox.setSelected(true);
        }
        if (user.getRoles().contains("instructor")) {
            instructorCheckBox.setSelected(true);
        }
        if (user.getRoles().contains("user")) {
            studentCheckBox.setSelected(true);
        }

        Button selectButton = new Button("Select Roles");
        selectButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        selectButton.setPadding(new Insets(10, 20, 10, 20));
        selectButton.setOnAction((ActionEvent event) -> {
            Set<String> selectedRoles = new HashSet<>();

            if (adminCheckBox.isSelected()) {
                selectedRoles.add("admin");
            }
            if (instructorCheckBox.isSelected()) {
                selectedRoles.add("instructor");
            }
            if (studentCheckBox.isSelected()) {
                selectedRoles.add("user");
            }

            // Update user's roles
            user.setRoles(new ArrayList<>(selectedRoles));

            // Determine the highest role and navigate accordingly
            String highestRole = getHighestRole(selectedRoles);
            if ("admin".equals(highestRole)) {
                AdminHomePage.RegisterWithNavigation(user);
                Navigation.navigateTo("AdminHomePage");
            } else if ("instructor".equals(highestRole)) {
                InstructorHomePage.RegisterWithNavigation(user);
                Navigation.navigateTo("InstructorHomePage");
            } else {
                UserHomePage.RegisterWithNavigation(user);
                Navigation.navigateTo("UserHomePage");
            }
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, adminCheckBox, instructorCheckBox, studentCheckBox, selectButton);

        Scene scene = new Scene(layout, 350, 250);
        Navigation.registerScene("RoleSelection", scene);
    }

    private static String getHighestRole(Set<String> roles) {
        List<String> sortedRoles = new ArrayList<>(roles);
        sortedRoles.sort(Comparator.comparingInt(role -> {
            if ("admin".equals(role)) return 3;
            if ("instructor".equals(role)) return 2;
            if ("user".equals(role)) return 1;
            return 0;
        }).reversed());
        return sortedRoles.isEmpty() ? "user" : sortedRoles.get(0);
    }
}
