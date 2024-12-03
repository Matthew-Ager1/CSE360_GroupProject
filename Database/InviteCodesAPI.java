package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;

public class InviteCodesAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> inviteCodesCollection = database.getCollection("InviteCodes");

    // Method to add a new invite code
    public static Boolean addInviteCode(String inviteCode) {
        try {
            // Check if invite code already exists
            if (inviteCodeExists(inviteCode)) {
                System.out.println("Invite code already exists.");
                return false;
            }

            Document inviteCodeDoc = new Document("inviteCode", inviteCode);
            inviteCodesCollection.insertOne(inviteCodeDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to check if an input text matches any invite code
    public static Boolean isInviteCodeValid(String inputText) {
        try {
            Document inviteCodeDoc = inviteCodesCollection.find(Filters.eq("inviteCode", inputText)).first();
            return inviteCodeDoc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve all invite codes
    public static List<String> getAllInviteCodes() {
        try {
            return inviteCodesCollection.find()
                .map(doc -> doc.getString("inviteCode"))
                .into(new java.util.ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Private helper method to check if an invite code exists
    private static Boolean inviteCodeExists(String inviteCode) {
        try {
            Document inviteCodeDoc = inviteCodesCollection.find(Filters.eq("inviteCode", inviteCode)).first();
            return inviteCodeDoc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
