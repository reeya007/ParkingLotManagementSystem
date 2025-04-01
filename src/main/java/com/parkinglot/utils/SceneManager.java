package com.parkinglot.utils;

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
            stage.setMaximized(true);
            scene.getStylesheets().add(stylesheet);
            stage.show();
        } catch (IOException e) {
            // Handle the exception (e.g., log the error, show an alert)
            System.err.println("Error loading FXML: " + fxmlFile);
            e.printStackTrace(); // Print the stack trace for debugging
            throw e; // Re-throw the exception to notify the caller
        }
    }
}