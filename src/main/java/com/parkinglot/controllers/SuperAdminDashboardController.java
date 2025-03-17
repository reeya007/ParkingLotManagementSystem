package com.parkinglot.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SuperAdminDashboardController {

    public void handleAddAdmin(ActionEvent event) throws IOException {
        loadScene("admin_registration.fxml", "Admin Registration", event);
    }

    public void handleViewUsers(ActionEvent event) throws IOException {
        loadScene("view_users.fxml", "View Users", event);
    }

    public void handleAddVehicleType(ActionEvent event) throws IOException {
        loadScene("add_vehicle_type.fxml", "Add Vehicle Type", event);
    }

    public void handleAddParkingSlot(ActionEvent event) throws IOException {
        loadScene("add_parking_slot.fxml", "Add Parking Slot", event);
    }


    private void loadScene(String fxmlFile, String title, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/parkinglot/views/" + fxmlFile));
        Stage stage = (Stage) ((javafx.scene.Node) javafx.scene.control.Button.class.cast(event.getSource())).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}