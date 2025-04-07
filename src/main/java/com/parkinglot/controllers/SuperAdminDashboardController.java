package com.parkinglot.controllers;

import com.parkinglot.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.parkinglot.utils.SceneManager.loadScene;

public class SuperAdminDashboardController {

    public void handleAddAdmin(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/admin_registration.css").toExternalForm();
        loadScene("admin_registration.fxml", "Admin Registration", event, stylesheet);
    }

    public void handleViewUsers(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/view_users.css").toExternalForm();
        loadScene("view_users.fxml", "View Users", event, stylesheet);

    }

    public void handleAddVehicleType(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/add_vehicle_type.css").toExternalForm();
        loadScene("add_vehicle_type.fxml", "Add Vehicle Type", event, stylesheet);
    }

    public void handleAddParkingSlot(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource( "/com/parkinglot/styles/add_parking_slot.css").toExternalForm();
        loadScene("add_parking_slot.fxml", "Add Parking Slot", event, stylesheet );

    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/login.css").toExternalForm();
        SceneManager.loadScene("login.fxml", "Parking Lot Management Systems", event, stylesheet);
    }
}