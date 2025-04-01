package com.parkinglot.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.parkinglot.utils.SceneManager.loadScene;

public class WelcomeController {

    public void handleLogin(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/login.css").toExternalForm();
        loadScene("login.fxml", "Login", event, stylesheet);
    }

    public void handleCustomerRegistration(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/customer_registration.css").toExternalForm();
        loadScene("customer_registration.fxml", "Customer Registration", event, stylesheet);
    }

}