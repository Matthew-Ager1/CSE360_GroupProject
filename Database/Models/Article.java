package Database.Models;

import java.util.List;

public class Article {
    private long id; // Unique long integer identifier
    private String level; // Level of the article (e.g., beginner, intermediate, advanced, expert)
    private List<String> groups; // Grouping identifiers
    private boolean sensitive; // Indicates if the article is sensitive/restricted
    private String title;
    private String shortDescription;
    private List<String> keywords; // Keywords or phrases for search
    private String body; // Body of the help article
    private List<String> links; // Links to reference materials and related articles
    private String nonSensitiveTitle; // Title free of sensitive information
    private String nonSensitiveDescription; // Description free of sensitive information

    // Constructors
    public Article() {}

    public Article(long id, String level, List<String> groups, boolean sensitive, String title,
                   String shortDescription, List<String> keywords, String body, List<String> links,
                   String nonSensitiveTitle, String nonSensitiveDescription) {
        this.id = id;
        this.level = level;
        this.groups = groups;
        this.sensitive = sensitive;
        this.title = title;
        this.shortDescription = shortDescription;
        this.keywords = keywords;
        this.body = body;
        this.links = links;
        this.nonSensitiveTitle = nonSensitiveTitle;
        this.nonSensitiveDescription = nonSensitiveDescription;
    }

    // Getters and Setters for all fields

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public String getNonSensitiveTitle() {
        return nonSensitiveTitle;
    }

    public void setNonSensitiveTitle(String nonSensitiveTitle) {
        this.nonSensitiveTitle = nonSensitiveTitle;
    }

    public String getNonSensitiveDescription() {
        return nonSensitiveDescription;
    }

    public void setNonSensitiveDescription(String nonSensitiveDescription) {
        this.nonSensitiveDescription = nonSensitiveDescription;
    }
}
