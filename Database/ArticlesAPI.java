package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import Database.Models.Article;
import Database.Models.Group;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArticlesAPI {
    private static final MongoDatabase database = DatabaseUtil.getDatabase();
    private static final MongoCollection<Document> articlesCollection = database.getCollection("Articles");
    private static final MongoCollection<Document> countersCollection = database.getCollection("Counters");

    // Method to generate unique long integer IDs
    private static long getNextSequence(String name) {
        Document filter = new Document("_id", name);
        Document update = new Document("$inc", new Document("seq", 1));
        Document result = countersCollection.findOneAndUpdate(filter, update);
        if (result == null) {
            countersCollection.insertOne(new Document("_id", name).append("seq", 1L));
            return 1L;
        } else {
            return result.getLong("seq") + 1;
        }
    }

    // Asynchronous method to add a new article
    public static CompletableFuture<Boolean> addArticleAsync(Article article) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Generate unique ID for the article
                long id = getNextSequence("articleid");
                article.setId(id);

                // Determine if the article belongs to a Special Access Group
                boolean isSAG = isArticleInSpecialAccessGroup(article.getGroups());

                // Encrypt body if in SAG
                if (isSAG) {
                    String encryptedBody = PasswordUtil.hashPassword(article.getBody());
                    article.setBody(encryptedBody);
                }

                // Create Document to insert
                Document articleDoc = articleToDocument(article);
                articlesCollection.insertOne(articleDoc);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    // Asynchronous method to update an existing article
    public static CompletableFuture<Boolean> updateArticleAsync(Article article) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Bson filter = Filters.eq("id", article.getId());

                // Determine if the article belongs to a Special Access Group
                boolean isSAG = isArticleInSpecialAccessGroup(article.getGroups());

                // Encrypt body if in SAG
                if (isSAG) {
                    String encryptedBody = PasswordUtil.hashPassword(article.getBody());
                    article.setBody(encryptedBody);
                }

                Document updateDoc = new Document()
                        .append("level", article.getLevel())
                        .append("groups", article.getGroups())
                        .append("sensitive", article.isSensitive())
                        .append("title", article.getTitle())
                        .append("shortDescription", article.getShortDescription())
                        .append("keywords", article.getKeywords())
                        .append("body", article.getBody())
                        .append("links", article.getLinks())
                        .append("nonSensitiveTitle", article.getNonSensitiveTitle())
                        .append("nonSensitiveDescription", article.getNonSensitiveDescription());

                articlesCollection.updateOne(filter, new Document("$set", updateDoc));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    // Asynchronous method to delete an article
    public static CompletableFuture<Boolean> deleteArticleAsync(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Bson filter = Filters.eq("id", id);
                articlesCollection.deleteOne(filter);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    // Asynchronous method to get an article by ID
    public static CompletableFuture<Article> getArticleAsync(long id, String requestingUsername) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Bson filter = Filters.eq("id", id);
                Document doc = articlesCollection.find(filter).first();
                if (doc != null) {
                    Article article = documentToArticle(doc);
                    // Check if article is in SAG
                    boolean isSAG = isArticleInSpecialAccessGroup(article.getGroups());
                    if (isSAG) {
                        // Check if user has permission to view decrypted body
                        if (true) {//TODO: Use username to check permission
                            String decryptedBody = PasswordUtil.hashPassword(article.getBody());
                            article.setBody(decryptedBody);
                        }
                    }
                    return article;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    // Asynchronous method to list all articles
    public static CompletableFuture<List<Article>> listArticlesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<Article> articles = new ArrayList<>();
            try {
                for (Document doc : articlesCollection.find()) {
                    articles.add(documentToArticle(doc));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return articles;
        });
    }

    // Asynchronous method to list articles by groups
    public static CompletableFuture<List<Article>> listArticlesByGroupsAsync(List<String> groups) {
        return CompletableFuture.supplyAsync(() -> {
            List<Article> articles = new ArrayList<>();
            try {
                Bson filter = Filters.in("groups", groups);
                for (Document doc : articlesCollection.find(filter)) {
                    articles.add(documentToArticle(doc));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return articles;
        });
    }

    // Asynchronous method to backup articles
    public static CompletableFuture<Boolean> backupArticlesAsync(String filename, List<String> groups) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Article> articlesToBackup;
                if (groups == null || groups.isEmpty()) {
                    articlesToBackup = listArticlesAsync().join();
                } else {
                    articlesToBackup = listArticlesByGroupsAsync(groups).join();
                }
                // Convert articles to JSON and save to file using your preferred JSON library
                // Example using Gson:
                /*
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try (FileWriter writer = new FileWriter(filename)) {
                    gson.toJson(articlesToBackup, writer);
                }
                */
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    // Asynchronous method to restore articles
    public static CompletableFuture<Boolean> restoreArticlesAsync(String filename, List<String> groups, boolean removeExisting) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Read articles from file using your preferred JSON library
                // Example using Gson:
                /*
                Gson gson = new Gson();
                try (FileReader reader = new FileReader(filename)) {
                    List<Article> articlesFromBackup = gson.fromJson(reader, new TypeToken<List<Article>>(){}.getType());
                    if (removeExisting) {
                        if (groups == null || groups.isEmpty()) {
                            articlesCollection.deleteMany(new Document());
                        } else {
                            Bson filter = Filters.in("groups", groups);
                            articlesCollection.deleteMany(filter);
                        }
                    }
                    for (Article article : articlesFromBackup) {
                        // Encrypt body if in SAG
                        boolean isSAG = isArticleInSpecialAccessGroup(article.getGroups());
                        if (isSAG) {
                            String encryptedBody = EncryptionUtil.encrypt(article.getBody());
                            article.setBody(encryptedBody);
                        }
                        Document articleDoc = articleToDocument(article);
                        articlesCollection.insertOne(articleDoc);
                    }
                }
                */
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    // Helper method to determine if an article is in a Special Access Group
    private static boolean isArticleInSpecialAccessGroup(List<String> groups) {
        for (String groupName : groups) {
            Group group = GroupsAPI.getGroupByName(groupName);
            if (group != null && group.isSpecialAccess()) {
                return true;
            }
        }
        return false;
    }

    // Helper method to convert Article to Document
    private static Document articleToDocument(Article article) {
        Document articleDoc = new Document("id", article.getId())
                .append("level", article.getLevel())
                .append("groups", article.getGroups())
                .append("sensitive", article.isSensitive())
                .append("title", article.getTitle())
                .append("shortDescription", article.getShortDescription())
                .append("keywords", article.getKeywords())
                .append("body", article.getBody())
                .append("links", article.getLinks())
                .append("nonSensitiveTitle", article.getNonSensitiveTitle())
                .append("nonSensitiveDescription", article.getNonSensitiveDescription());
        return articleDoc;
    }

    // Helper method to convert Document to Article
    private static Article documentToArticle(Document doc) {
        Article article = new Article();
        article.setId(doc.getLong("id"));
        article.setLevel(doc.getString("level"));
        article.setGroups((List<String>) doc.get("groups"));
        article.setSensitive(doc.getBoolean("sensitive", false));
        article.setTitle(doc.getString("title"));
        article.setShortDescription(doc.getString("shortDescription"));
        article.setKeywords((List<String>) doc.get("keywords"));
        article.setBody(doc.getString("body"));
        article.setLinks((List<String>) doc.get("links"));
        article.setNonSensitiveTitle(doc.getString("nonSensitiveTitle"));
        article.setNonSensitiveDescription(doc.getString("nonSensitiveDescription"));
        return article;
    }
}
