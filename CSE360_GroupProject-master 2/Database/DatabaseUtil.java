package Database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseUtil {
    private static final String CONNECTION_STRING = "mongodb+srv://GlobalUser:GlobalUserPassword@hotspotcluster1.4r0bf.mongodb.net/Users?retryWrites=true&w=majority&appName=HotspotCluster1";//System.getenv("MONGODB_URI"); // Ensure this is set
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        try {
            if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
                throw new IllegalArgumentException("MONGODB_URI environment variable is not set.");
            }

            ConnectionString connString = new ConnectionString(CONNECTION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(connString.getDatabase());
            System.out.println("Connected to MongoDB Atlas successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to MongoDB Atlas.");
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}