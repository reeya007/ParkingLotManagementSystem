package com.parkinglot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Parking Lot Management System");
        stage.setScene(scene);

        String stylesheet = getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
