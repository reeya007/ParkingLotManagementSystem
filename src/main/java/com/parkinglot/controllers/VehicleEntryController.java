package com.parkinglot.controllers;

import com.parkinglot.services.VehicleEntryAndExitService;
import com.parkinglot.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

import static com.parkinglot.utils.SceneManager.loadScene;

/**
 * Controller for the "Vehicle Entry" screen.
 * Handles the process of recording a vehicle entering the parking lot.
 */
public class VehicleEntryController {

    @FXML
    private TextField licensePlateField;

    private final VehicleEntryAndExitService vehicleEntryService = new VehicleEntryAndExitService();

    /**
     * Handles the action when a vehicle attempts to enter the parking lot.
     * Retrieves the license plate from the input, delegates the entry logic to the VehicleEntryService,
     * and updates the UI based on the outcome.
     */
    @FXML
    public void handleVehicleEntry() {
        String licensePlate = licensePlateField.getText();

        try {
            vehicleEntryService.processVehicleEntry(licensePlate);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle entered successfully.");
            licensePlateField.clear();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Vehicle entry failed: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", e.getMessage());
            licensePlateField.clear();
        }
    }

    /**
     * Handles the action when the "Back" button is clicked.
     * Navigates the user back to the Admin Dashboard.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an error loading the Admin Dashboard scene.
     */
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/admin_dashboard.css").toExternalForm();
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event, adminStylesheet);
    }

    /**
     * Displays an alert dialog with the specified type, title, and content.
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert dialog.
     * @param content The message content of the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertUtil.showAlert(alertType, title, content);
    }
}