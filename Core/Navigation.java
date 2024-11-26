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
    private static Object currentData; // Store additional data (e.g., User object)

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void registerScene(String name, Scene scene) {
            scenes.put(name, scene);
    }

    public static void navigateTo(String sceneName, Object data) {
        currentData = data;
        Scene scene = scenes.get(sceneName);
        if (scene != null) {
            sceneHistory.push(sceneName);
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.err.println("Scene '" + sceneName + "' not found.");
        }
    }

    public static void goBack() {
        if (sceneHistory.size() > 1) {
            sceneHistory.pop();
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

    public static Object getCurrentData() {
        return currentData;
    }

    public static void clearAllScenes() {
        scenes.clear();
        sceneHistory.clear();
    }
}
