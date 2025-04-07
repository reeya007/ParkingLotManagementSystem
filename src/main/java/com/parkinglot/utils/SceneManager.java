package com.parkinglot.utils;

import com.parkinglot.controllers.UserAwareController;
import com.parkinglot.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    public static void loadScene(String fxmlFile, String title, ActionEvent event, String stylesheet) throws IOException {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/com/parkinglot/views/" + fxmlFile));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
//            stage.setMaximized(true);
            scene.getStylesheets().add(stylesheet);
            stage.show();
        } catch (IOException e) {
            // Handle the exception (e.g., log the error, show an alert)
            System.err.println("Error loading FXML: " + fxmlFile);
            e.printStackTrace(); // Print the stack trace for debugging
            throw e; // Re-throw the exception to notify the caller
        }
    }

    public static void loadScene(String fxmlFile, String title, ActionEvent event, String stylesheet, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/com/parkinglot/views/" + fxmlFile));
        Parent root = loader.load();

        UserAwareController controller = loader.getController();
        controller.setCurrentUser(user);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
//        stage.setMaximized(true);
        scene.getStylesheets().add(stylesheet);
        stage.show();
    }
}