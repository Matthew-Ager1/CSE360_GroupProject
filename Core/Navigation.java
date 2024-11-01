package Core;

import java.util.*;
import javafx.stage.*;
import javafx.scene.*;

public class Navigation {
	private static Stage primaryStage;
    private static final HashMap<String, Scene> scenes = new HashMap<>();

    // Set the primary stage (typically called once from your main application)
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Register a scene with a name for easy retrieval
    public static void registerScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    // Navigate to a specific scene by name
    public static void navigateTo(String sceneName) {
        Scene scene = scenes.get(sceneName);
        if (scene != null) {
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.out.println("Scene '" + sceneName + "' not found.");
        }
    }

    // Optional: Remove a scene if needed
    public static void removeScene(String name) {
        scenes.remove(name);
    }

    // Optional: Clear all scenes
    public static void clearAllScenes() {
        scenes.clear();
    }
}
