package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import Database.Models.Group;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class GroupsAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> groupsCollection = database.getCollection("Groups");

    // Method to create a new group
    public static Boolean createGroup(Group group) {
        try {
            // Check if group name already exists
            if (groupExists(group.getName())) {
                System.out.println("Group with the same name already exists.");
                return false;
            }
            Document groupDoc = groupToDocument(group);
            groupsCollection.insertOne(groupDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve a group by name
    public static Group getGroupByName(String groupName) {
        try {
            Document groupDoc = groupsCollection.find(Filters.eq("name", groupName)).first();
            if (groupDoc != null) {
                return documentToGroup(groupDoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to retrieve all groups
    public static List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        try {
            for (Document doc : groupsCollection.find()) {
                groups.add(documentToGroup(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    // Method to update a group's details
    public static Boolean updateGroup(Group group) {
        try {
            Bson filter = Filters.eq("name", group.getName());
            Bson update = Updates.combine(
                Updates.set("isSpecialAccess", group.isSpecialAccess()),
                Updates.set("adminUsers", group.getAdminUsers()),
                Updates.set("instructors", group.getInstructors()),
                Updates.set("students", group.getStudents())
            );
            groupsCollection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to delete a group by name
    public static Boolean deleteGroup(String groupName) {
        try {
            groupsCollection.deleteOne(Filters.eq("name", groupName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to add a user to a group's specific role
    public static Boolean addUserToGroupRole(String groupName, String username, String role) {
        try {
            Bson filter = Filters.eq("name", groupName);
            Bson update = Updates.addToSet(role, username); // role can be "adminUsers", "instructors", "students"
            groupsCollection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to remove a user from a group's specific role
    public static Boolean removeUserFromGroupRole(String groupName, String username, String role) {
        try {
            Bson filter = Filters.eq("name", groupName);
            Bson update = Updates.pull(role, username); // role can be "adminUsers", "instructors", "students"
            groupsCollection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Private helper methods

    private static Boolean groupExists(String groupName) {
        try {
            Document groupDoc = groupsCollection.find(Filters.eq("name", groupName)).first();
            return groupDoc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Group documentToGroup(Document doc) {
        Group group = new Group();
        group.setId(doc.getObjectId("_id"));
        group.setName(doc.getString("name"));
        group.setSpecialAccess(doc.getBoolean("isSpecialAccess", false));
        group.setAdminUsers((List<String>) doc.get("adminUsers"));
        group.setInstructors((List<String>) doc.get("instructors"));
        group.setStudents((List<String>) doc.get("students"));
        return group;
    }

    private static Document groupToDocument(Group group) {
        Document groupDoc = new Document("name", group.getName())
                            .append("isSpecialAccess", group.isSpecialAccess())
                            .append("adminUsers", group.getAdminUsers())
                            .append("instructors", group.getInstructors())
                            .append("students", group.getStudents());
        return groupDoc;
    }
}
