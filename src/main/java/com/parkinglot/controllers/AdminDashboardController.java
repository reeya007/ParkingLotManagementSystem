package com.parkinglot.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    public void handleViewSlots(ActionEvent event) throws IOException {
        loadScene("view_parking_slots.fxml", "View Parking Slots", event);
    }

    public void handleManageSlots(ActionEvent event) throws IOException {
        loadScene("manage_parking_slots.fxml", "Manage Parking Slots", event);
    }

    public void handleMonitorTransactions(ActionEvent event) throws IOException {
        loadScene("monitor_transactions.fxml", "Monitor Transactions", event);
    }

    public void handleVehicleEntry(ActionEvent event) throws IOException {
        loadScene("vehicle_entry.fxml", "Vehicle Entry", event);
    }

    public void handleVehicleExit(ActionEvent event) throws IOException {
        loadScene("vehicle_exit.fxml", "Vehicle Exit", event);
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