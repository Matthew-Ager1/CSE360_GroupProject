package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import Database.Models.User;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class UsersAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> usersCollection = database.getCollection("Users");

    // Method to add a new user
    public static Boolean addUser(User user) {
        try {
            if (usernameExists(user.getUsername()) || emailExists(user.getEmail())) {
                System.out.println("User with the same username or email already exists.");
                return false;
            }

            // Assign roles
            List<String> roles = new ArrayList<>();
            if (getAllUsers().isEmpty()) {
                roles.add("admin");
            } else {
                roles.add("user");
            }

            user.setRoles(roles);

            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

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
                return PasswordUtil.verifyPassword(password, storedHashedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to get a User object by username
    public static User getUser(String username) {
        System.out.println("Fetching user for username: " + username);
        Document userDoc = getUserDocumentByUsername(username);
        if (userDoc != null) {
            return documentToUser(userDoc);
        }
        System.out.println("User not found");
        return null;
    }

    // Method to get all users
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (Document doc : usersCollection.find()) {
            users.add(documentToUser(doc));
        }
        return users;
    }

    // Private method to check if a username exists
    private static Boolean usernameExists(String username) {
        try {
            Document userDoc = usersCollection.find(new Document("username", username)).first();
            return userDoc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private method to check if an email exists
    private static Boolean emailExists(String email) {
        try {
            Document userDoc = usersCollection.find(new Document("email", email)).first();
            return userDoc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private method to get user Document by username
    private static Document getUserDocumentByUsername(String username) {
        try {
            return usersCollection.find(new Document("username", username)).first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Private method to convert Document to User object
    private static User documentToUser(Document doc) {
        User user = new User();
        user.setEmail(doc.getString("email"));
        user.setUsername(doc.getString("username"));
        user.setPassword(doc.getString("password"));
        user.setName(doc.getString("name"));
        user.setOnePass(doc.getBoolean("one_pass", false));
        user.setExpire(doc.getInteger("expire", 0));
        user.setRoles(doc.getList("roles", String.class));
        return user;
    }

    // Private method to convert User object to Document
    private static Document userToDocument(User user) {
        Document userDoc = new Document("email", user.getEmail())
                            .append("username", user.getUsername())
                            .append("password", user.getPassword())
                            .append("name", user.getName())
                            .append("one_pass", user.isOnePass())
                            .append("expire", user.getExpire())
                            .append("roles", user.getRoles());
        return userDoc;
    }
}
