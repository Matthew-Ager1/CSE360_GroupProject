package Database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import Database.Models.Role;
import Database.Models.User;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class UsersAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> usersCollection = database.getCollection("Users");

    // Method to add a new user with role and group memberships
    public static Boolean addUser(User user) {
        try {
            // Check if username or email already exists
            if (usernameExists(user.getUsername()) || emailExists(user.getEmail())) {
                System.out.println("User with the same username or email already exists.");
                return false;
            }

            // Hash the password before storing
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            // Initialize groups if null
            if (user.getGroups() == null) {
                user.setGroups(new ArrayList<>());
            }

            Document userDoc = userToDocument(user);
            usersCollection.insertOne(userDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to validate user login
    public static Boolean isValidLogin(String username, String password) {
        try {
            Document userDoc = getUserDocumentByUsername(username);
            if (userDoc != null) {
                String storedHashedPassword = userDoc.getString("password");
                // Verify the password
                return PasswordUtil.verifyPassword(password, storedHashedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    // Method to retrieve users by group
    public static List<User> getUsersByGroup(String groupName) {
        List<User> users = new ArrayList<>();
        try {
            Bson filter = Filters.in("groups", groupName);
            for (Document doc : usersCollection.find(filter)) {
                users.add(documentToUser(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // Method to delete a user by username
    public static Boolean deleteUserByUsername(String username) {
        try {
            usersCollection.deleteOne(new Document("username", username));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update a user's roles
    public static Boolean updateUserRoles(String username, List<Role> newRoles) {
        try {
            Bson filter = Filters.eq("username", username);
            Bson update = Updates.set("role", newRoles.toString());
            usersCollection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private helper methods

    private static Boolean usernameExists(String username) {
        try {
            Document userDoc = usersCollection.find(new Document("username", username)).first();
            return userDoc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean emailExists(String email) {
        try {
            Document userDoc = usersCollection.find(new Document("email", email)).first();
            return userDoc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Document getUserDocumentByUsername(String username) {
        try {
            return usersCollection.find(new Document("username", username)).first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static User documentToUser(Document doc) {
        User user = new User();
        user.setId(doc.getObjectId("_id"));
        user.setEmail(doc.getString("email"));
        user.setUsername(doc.getString("username"));
        user.setPassword(doc.getString("password"));
        user.setName(doc.getString("name"));
        user.setOnePass(doc.getBoolean("onePass", false));
        user.setExpire(doc.getInteger("expire", 0));
        String roleStr = doc.getString("role");
        if (roleStr != null) {
            user.setRole(Role.valueOf(roleStr));
        }
        user.setGroups((List<String>) doc.get("groups"));
        return user;
    }

    private static Document userToDocument(User user) {
        Document userDoc = new Document("email", user.getEmail())
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("name", user.getName())
                .append("onePass", user.isOnePass())
                .append("expire", user.getExpire())
                .append("role", user.getRole().toString())
                .append("groups", user.getGroups());
        return userDoc;
    }
}
