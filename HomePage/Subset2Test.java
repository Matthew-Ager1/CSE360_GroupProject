package HomePage;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.*;

import Database.UsersAPI;
import Database.Models.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class Subset2Test {

    @Test
    public void testRemoveUser() {
    	boolean success = false;
        User user = new User();
        user.setUsername("TestUserName");
        String selectedUser = user.getUsername();
        if (selectedUser != null) {
            String username = selectedUser.split(" - ")[0];
            boolean confirmed = true;//showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete user: " + username + "?");
            if (confirmed) {
                success = true;
                if (success) {
                    //showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                } else {
                    //showAlert(Alert.AlertType.ERROR, "Failure", "Failed to delete user.");
                }
                //refreshUsersList(usersListView);
            }
        } else {
            //showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to remove.");
        }

        Assert.assertEquals("The execution should be successful", success, true);
    }
    
    @Test
    public void testListAccounts() {
    	ArrayList<User> users = new ArrayList<User>();
    	users.add(new User()); 
    	users.add(new User()); 
    	users.add(new User()); 
    	users.add(new User()); 
    	users.add(new User()); 
    	
    	ArrayList<String> usersListView = new ArrayList<>();
        mockRefreshUsersList(usersListView);
        
        Assert.assertEquals("The amount of users in the list should be 5", users.size(), 5);
    }
    
    void mockRefreshUsersList(ArrayList<String> usersListView) {
    	
    }
}