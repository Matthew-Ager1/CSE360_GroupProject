package Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import Database.Models.Article;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

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

    // Method to add a new article
    public static boolean addArticle(Article article) {
        try {
            // Generate unique ID for the article
            long id = getNextSequence("articleid");
            article.setId(id);

            // Create Document to insert
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

            articlesCollection.insertOne(articleDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update an existing article
    public static boolean updateArticle(Article article) {
        try {
            Bson filter = Filters.eq("id", article.getId());
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
    }

    // Method to delete an article
    public static boolean deleteArticle(long id) {
        try {
            Bson filter = Filters.eq("id", id);
            articlesCollection.deleteOne(filter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to get an article by ID
    public static Article getArticle(long id) {
        try {
            Bson filter = Filters.eq("id", id);
            Document doc = articlesCollection.find(filter).first();
            if (doc != null) {
                return documentToArticle(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to convert Document to Article
    private static Article documentToArticle(Document doc) {
        Article article = new Article();
        article.setId(doc.getLong("id"));
        article.setLevel(doc.getString("level"));
        article.setGroups((List<String>) doc.get("groups"));
        article.setSensitive(doc.getBoolean("sensitive"));
        article.setTitle(doc.getString("title"));
        article.setShortDescription(doc.getString("shortDescription"));
        article.setKeywords((List<String>) doc.get("keywords"));
        article.setBody(doc.getString("body"));
        article.setLinks((List<String>) doc.get("links"));
        article.setNonSensitiveTitle(doc.getString("nonSensitiveTitle"));
        article.setNonSensitiveDescription(doc.getString("nonSensitiveDescription"));
        return article;
    }

    // Method to list all articles
    public static List<Article> listArticles() {
        List<Article> articles = new ArrayList<>();
        try {
            for (Document doc : articlesCollection.find()) {
                articles.add(documentToArticle(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    // Method to list articles by group(s)
    public static List<Article> listArticlesByGroups(List<String> groups) {
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
    }

    // Method to backup articles to a file (in JSON format)
    public static boolean backupArticles(String filename, List<String> groups) {
        try {
            List<Article> articlesToBackup;
            if (groups == null || groups.isEmpty()) {
                articlesToBackup = listArticles();
            } else {
                articlesToBackup = listArticlesByGroups(groups);
            }
            // Convert articles to JSON and save to file
            // Implementation depends on chosen JSON library (e.g., Gson or Jackson)
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    // OKAY THIS IS LITERALLY THE SAME BUT LINKED DIFFERENTLY
    public static boolean backupArticles(Set<Long> backup_targets) {	
    	try {
    		List<Article> master_list = listArticles();
    		List<Article> articlesToBackup = new ArrayList<Article>();
    		
    		for (Long ID : backup_targets) {
    			for (Article a : master_list) {
    				if (a.getId() == ID) {
    					articlesToBackup.add(a);
    				}
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    
    	return false;
    }
    
 // OKAY THIS IS LITERALLY THE SAME BUT LINKED DIFFERENTLY
    public static boolean restoreArticles(Set<Long> to_restore) {
        try {
            // Read articles from file (JSON)
            // Implementation depends on chosen JSON library (e.g., Gson or Jackson)
            List<Article> articlesFromBackup = new ArrayList<>(); // Populate this list from the file

            for (Article article : articlesFromBackup) {
                // Check if article with the same ID exists
                Bson filter = Filters.eq("id", article.getId());
                Document existingArticle = articlesCollection.find(filter).first();
                if (existingArticle == null) {
                    // Insert the article
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

                    articlesCollection.insertOne(articleDoc);
                }
                // If IDs match, do not add the article
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to restore articles from a file
    public static boolean restoreArticles(String filename, boolean removeExisting) {
        try {
            // Read articles from file (JSON)
            // Implementation depends on chosen JSON library (e.g., Gson or Jackson)
            List<Article> articlesFromBackup = new ArrayList<>(); // Populate this list from the file

            if (removeExisting) {
                // Remove all existing articles
                articlesCollection.deleteMany(new Document());
            }

            for (Article article : articlesFromBackup) {
                // Check if article with the same ID exists
                Bson filter = Filters.eq("id", article.getId());
                Document existingArticle = articlesCollection.find(filter).first();
                if (existingArticle == null) {
                    // Insert the article
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

                    articlesCollection.insertOne(articleDoc);
                }
                // If IDs match, do not add the article
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
