package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Database.Models.User;

import org.bson.Document;

public class UsersAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> usersCollection = database.getCollection("Users");

    // All new methods in this class are private!!!
    
    // Method to add a new user
    public static Boolean addUser(User user) {
        try {
            // Check if username or email already exists
            Document existingUser = usersCollection.find(new Document("$or", 
                java.util.Arrays.asList(
                    new Document("username", user.getUsername()),
                    new Document("email", user.getEmail())
                ))).first();
            
            if (existingUser != null) {
                System.out.println("User with the same username or email already exists.");
                return false;
            }

            // Hash the password before storing
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            
            Document userDoc = new Document("email", user.getEmail())
                                .append("username", user.getUsername())
                                .append("password", user.getPassword())
                                .append("name", user.getName())
                                .append("one_pass", user.isOnePass())
                                .append("expire", user.getExpire())
                                .append("perms", user.getPerms());
            
            usersCollection.insertOne(userDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Boolean isValidLogin(String username, String password) {
        try {
            Document userDoc = usersCollection.find(new Document("username", username)).first();
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
}