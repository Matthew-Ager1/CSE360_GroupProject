package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import Database.Models.Role;
import Database.Models.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;
import Database.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;

public class UsersAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> usersCollection = database.getCollection("Users");

    // Method to add a new user with role and group memberships
    public static Boolean addUser(User user) {
        try {
            // Check if the username or email already exists
            if (usernameExists(user.getUsername())) {
                System.out.println("User with the same username already exists.");
                return false;
            }

            // Hash the password before storing
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            // Initialize groups if null
            if (user.getGroups() == null) {
                user.setGroups(new ArrayList<>());
            }

            // Convert User object to Document and insert into the collection
            Document userDoc = userToDocument(user);
            usersCollection.insertOne(userDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to validate user login
    public static boolean isValidLogin(String username, String enteredPassword) {
        // Retrieve the user by username
        User storedUser = getUserByUsername(username);
        if (storedUser != null) {
            // Compare the entered password with the stored hashed password
            return PasswordUtil.verifyPassword(enteredPassword, storedUser.getPassword());
        }
        return false;  // Return false if user not found
    }

    // Method to retrieve user by username
    public static User getUserByUsername(String username) {
        try {
            Document userDoc = getUserDocumentByUsername(username);
            if (userDoc != null) {
                return documentToUser(userDoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to retrieve all users
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            for (Document doc : usersCollection.find()) {
                users.add(documentToUser(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // Method to add a user to a group
    public static Boolean addUserToGroup(String username, String groupName) {
        try {
            Bson filter = Filters.eq("username", username);
            Bson update = Updates.addToSet("groups", groupName);
            usersCollection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to remove a user from a group
    public static Boolean removeUserFromGroup(String username, String groupName) {
        try {
            Bson filter = Filters.eq("username", username);
            Bson update = Updates.pull("groups", groupName);
            usersCollection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update a user's roles
    public static Boolean updateUserRoles(String username, Set<Role> newRoles) {
        try {
            Bson filter = Filters.eq("username", username);
            Bson update = Updates.set("roles", convertRolesToStrings(newRoles));
            usersCollection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update user information
    public static boolean updateUser(User user) {
        if (user == null) {
            System.out.println("User object or username is null. Cannot proceed with update.");
            return false;
        }

        try {
            // Log the user details before updating
            System.out.println("Updating user: " + user.getUsername());

            // Validate and hash the password
            if (user.getPassword() == null) {
                System.out.println("Password is null. Cannot proceed with update.");
                return false;
            }
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            // Define the filter to locate the user in the database
            Bson filter = Filters.eq("username", user.getUsername());

            // Combine updates for all fields
            Bson update = Updates.combine(
                    Updates.set("email", user.getEmail()),
                    Updates.set("firstName", user.getFirstName()),
                    Updates.set("middleName", user.getMiddleName()),
                    Updates.set("lastName", user.getLastName()),
                    Updates.set("preferredName", user.getPreferredName()),
                    Updates.set("password", hashedPassword),  // Use hashed password
                    Updates.set("isFinished", true)           // Mark the account setup as finished
            );

            // Print the filter and update details for debugging
            System.out.println("Filter: " + filter.toString());
            System.out.println("Update Document: " + update.toString());

            // Execute the update operation
            UpdateResult result = usersCollection.updateOne(filter, update);

            // Check the result of the update operation
            if (result.getModifiedCount() > 0) {
                System.out.println("User update successful.");
                return true;
            } else {
                System.out.println("No fields were updated. Verify if the user exists or if the data is unchanged.");
                return false;
            }
        } catch (Exception e) {
            // Log any exceptions with more context
            System.err.println("An error occurred while updating the user:");
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a user by username
    public static Boolean deleteUserByUsername(String username) {
        try {
            Bson filter = Filters.eq("username", username);
            usersCollection.deleteOne(filter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean usernameExists(String username) {
        return getUserDocumentByUsername(username) != null;
    }

    private static boolean emailExists(String email) {
        return usersCollection.find(Filters.eq("email", email)).first() != null;
    }

    private static Document getUserDocumentByUsername(String username) {
        return usersCollection.find(new Document("username", username)).first();
    }

    private static User documentToUser(Document doc) {
        User user = new User();

        // Handling null or missing fields
        try {
            user.setId(doc.getObjectId("_id").toString());
            user.setEmail(doc.getString("email"));
            user.setUsername(doc.getString("username"));
            user.setFirstName(doc.getString("firstName"));
            user.setMiddleName(doc.getString("middleName"));
            user.setLastName(doc.getString("lastName"));
            user.setOnePass(doc.getBoolean("onePass", false));  // Default to false if null
            user.setExpire(doc.getInteger("expire", 0)); // Default to 0 if null
            user.setPassword(doc.getString("password"));
            user.setPreferredName(doc.getString("preferredName"));

            // Handling Timestamp conversion for password expiration date
            Object passwordExpirationObj = doc.get("passwordExpirationDate");
            if (passwordExpirationObj instanceof Timestamp) {
                user.setPasswordExpirationDate(((Timestamp) passwordExpirationObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            } else {
                // Handle null or missing passwordExpirationDate by setting a default value
                user.setPasswordExpirationDate(LocalDateTime.now());  // Use the current time if the field is missing or invalid
            }


            user.setIsFinished(doc.getBoolean("isFinished", false)); // Default to false if null

            // Converting the list of role names to Role enum set
            List<String> roleNames = doc.getList("roles", String.class);
            Set<Role> roles = new HashSet<>();
            if (roleNames != null) {
                for (String roleName : roleNames) {
                    try {
                        roles.add(Role.valueOf(roleName));  // Convert string to Role enum
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid role: " + roleName);  // Handle invalid roles
                    }
                }
            }
            user.setRoles(roles);

            // Handling the list of group memberships
            List<String> groups = doc.getList("groups", String.class);
            if (groups != null) {
                user.setGroups(groups);
            } else {
                user.setGroups(new ArrayList<>());  // Initialize to empty list if null
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }


    private static Document userToDocument(User user) {
        Document doc = new Document();
        doc.put("email", user.getEmail());
        doc.put("username", user.getUsername());
        doc.put("firstName", user.getFirstName());
        doc.put("middleName", user.getMiddleName());
        doc.put("lastName", user.getLastName());
        doc.put("onePass", user.isOnePass());
        doc.put("expire", user.getExpire());
        doc.put("password", user.getPassword());  // Store the password securely
        doc.put("preferredName", user.getPreferredName());

        // Handle null password expiration date
        if (user.getPasswordExpirationDate() != null) {
            doc.put("passwordExpirationDate", Timestamp.valueOf(user.getPasswordExpirationDate()));
        } else {
            // If null, you can set a default value or skip storing it
            doc.put("passwordExpirationDate", Timestamp.valueOf(LocalDateTime.now()));  // Default to current time
        }

        doc.put("isFinished", user.getIsFinished());

        // Convert roles to strings
        List<String> roleNames = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roleNames.add(role.name());
        }
        doc.put("roles", roleNames);
        doc.put("groups", user.getGroups());
        return doc;
    }

    private static List<String> convertRolesToStrings(Set<Role> roles) {
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.name());
        }
        return roleNames;
    }
}
