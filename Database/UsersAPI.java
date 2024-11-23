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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsersAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> usersCollection = database.getCollection("Users");

    // Method to add a new user with role and group memberships
    public static Boolean addUser(User user) {
        try {
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

    // Method to update a user's roles
    public static Boolean updateUserRoles(String username, Set<Role> newRoles) {
        try {
            Bson filter = Filters.eq("username", username);
            Bson update = Updates.set("roles", new ArrayList<>(newRoles));
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
            return usersCollection.find(new Document("username", username)).first() != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean emailExists(String email) {
        try {
            return usersCollection.find(new Document("email", email)).first() != null;
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

        List<String> rolesList = doc.getList("roles", String.class);
        Set<Role> roles = new HashSet<>();
        if (rolesList != null) {
            for (String roleStr : rolesList) {
                try {
                    // Convert role to enum (case insensitive)
                    roles.add(Role.valueOf(roleStr.toUpperCase()));  // Ensure uppercase for consistency
                } catch (IllegalArgumentException e) {
                    // Handle invalid roles, e.g., log or set to default
                    System.out.println("Invalid role in database: " + roleStr);
                    // Optionally, you could assign a default role here:
                    // roles.add(Role.STUDENT);
                }
            }
        }
        user.setRoles(roles);

        user.setGroups(doc.getList("groups", String.class));
        return user;
    }

    private static Document userToDocument(User user) {
        return new Document("email", user.getEmail())
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("name", user.getName())
                .append("onePass", user.isOnePass())
                .append("expire", user.getExpire())
                .append("roles", new ArrayList<>(user.getRoles()))
                .append("groups", user.getGroups());
    }
    public static Boolean deleteUserByUsername(String username) {
        try {
            usersCollection.deleteOne(Filters.eq("username", username));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
