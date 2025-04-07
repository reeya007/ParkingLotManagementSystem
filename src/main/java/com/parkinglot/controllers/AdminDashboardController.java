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

public class AdminDashboardController {

    public void handleViewSlots(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/view_parking_slots.css").toExternalForm();
        loadScene("view_parking_slots.fxml", "View Parking Slots", event, adminStylesheet);
    }

    public void handleManageSlots(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/manage_parking_slots.css").toExternalForm();

        loadScene("manage_parking_slots.fxml", "Manage Parking Slots", event, adminStylesheet);
    }

    public void handleMonitorTransactions(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/monitor_transaction.css").toExternalForm();
        loadScene("monitor_transactions.fxml", "Monitor Transactions", event, adminStylesheet );
    }

    public void handleVehicleEntry(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/vehicle_entry.css").toExternalForm();
        loadScene("vehicle_entry.fxml", "Vehicle Entry", event, adminStylesheet);
    }

    public void handleVehicleExit(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/vehicle_exit.css").toExternalForm();
        loadScene("vehicle_exit.fxml", "Vehicle Exit", event, adminStylesheet);
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        SceneManager.loadScene("welcome.fxml", "Parking Lot Management System", event, adminStylesheet);
    }
}