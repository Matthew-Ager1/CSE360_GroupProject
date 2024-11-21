package Core;

public class DataHolder {
    private static DataHolder instance = null;
    private long selectedArticleId;

    private DataHolder() {}

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public long getSelectedArticleId() {
        return selectedArticleId;
    }

    public void setSelectedArticleId(long selectedArticleId) {
        this.selectedArticleId = selectedArticleId;
    }
}
