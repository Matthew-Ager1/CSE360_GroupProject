package Core;

import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class Navigation {
    private static Stage primaryStage;
    private static final Map<String, Scene> scenes = new HashMap<>();
    private static final Stack<String> sceneHistory = new Stack<>();

    // Set the primary stage (typically called once from your main application)
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Get the primary stage
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    // Register a scene with a name for easy retrieval
    public static void registerScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    // Navigate to a specific scene by name and add it to the history stack
    public static void navigateTo(String sceneName) {
        Scene scene = scenes.get(sceneName);
        if (scene != null) {
            // Push the current scene onto the stack before navigating to the new scene
            sceneHistory.push(sceneName);
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.out.println("Scene '" + sceneName + "' not found.");
        }
    }

    // Navigate back to the previous scene in history
    public static void goBack() {
        if (sceneHistory.size() > 1) {
            // Pop the current scene
            sceneHistory.pop();
            // Get the previous scene name and navigate to it
            String previousSceneName = sceneHistory.peek();
            Scene previousScene = scenes.get(previousSceneName);
            if (previousScene != null) {
                primaryStage.setScene(previousScene);
                primaryStage.show();
            }
        } else {
            System.out.println("No previous scene in history.");
        }
    }

    // Optional: Remove a scene if needed
    public static void removeScene(String name) {
        scenes.remove(name);
    }

    // Optional: Clear all scenes
    public static void clearAllScenes() {
        scenes.clear();
        sceneHistory.clear();
    }
}


