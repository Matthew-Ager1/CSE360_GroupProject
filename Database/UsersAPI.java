package Database; 

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import Database.Models.User;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UsersAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> usersCollection = database.getCollection("Users");

    // All new methods in this class are private!!!
    
    // Method to add a new user
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
            
            Document userDoc = userToDocument(user);
            usersCollection.insertOne(userDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
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

    // Private method to get user Document by email
    private static Document getUserDocumentByEmail(String email) {
        try {
            return usersCollection.find(new Document("email", email)).first();
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
        user.setExpire(doc.getDate("expire"));
        user.setPerms(doc.getList("perms", String.class));
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
                            .append("perms", user.getPerms());
        return userDoc;
    }

    // Private method to update a user's field
    private static Boolean updateUserField(String username, String fieldName, Object value) {
        try {
            Document update = new Document("$set", new Document(fieldName, value));
            usersCollection.updateOne(new Document("username", username), update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private method to delete a user by username
    private static Boolean deleteUserByUsername(String username) {
        try {
            usersCollection.deleteOne(new Document("username", username));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private method to change a user's password
    private static Boolean changeUserPassword(String username, String newPassword) {
        try {
            // Hash the new password before storing
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            return updateUserField(username, "password", hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private method to get all users
    private static List<User> getAllUsers() {
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

    // Private method to check if user has a specific permission
    private static Boolean userHasPermission(String username, String permission) {
        try {
            Document userDoc = getUserDocumentByUsername(username);
            if (userDoc != null) {
                List<String> perms = userDoc.getList("perms", String.class);
                return perms != null && perms.contains(permission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private method to add a permission to a user
    private static Boolean addPermissionToUser(String username, String permission) {
        try {
            Document update = new Document("$addToSet", new Document("perms", permission));
            usersCollection.updateOne(new Document("username", username), update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private method to remove a permission from a user
    private static Boolean removePermissionFromUser(String username, String permission) {
        try {
            Document update = new Document("$pull", new Document("perms", permission));
            usersCollection.updateOne(new Document("username", username), update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
