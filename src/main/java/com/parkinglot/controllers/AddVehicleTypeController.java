package com.parkinglot.controllers;

import com.parkinglot.services.VehicleTypeService;
import com.parkinglot.utils.AlertUtil;
import com.parkinglot.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the "Add Vehicle Type" screen.
 * Handles user input for creating new vehicle types.
 */
public class AddVehicleTypeController {

    @FXML
    private TextField typeNameField;

    @FXML
    private TextField hourlyRateField;

    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();

    /**
     * Handles the action when the "Add Vehicle Type" button is clicked.
     * Retrieves user input, validates it, and calls the VehicleTypeService to add the new vehicle type.
     */
    @FXML
    public void handleAddVehicleType() {
        String typeName = typeNameField.getText();
        String hourlyRateStr = hourlyRateField.getText();

        if (typeName.isEmpty() || hourlyRateStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            double hourlyRate = Double.parseDouble(hourlyRateStr);
            vehicleTypeService.addVehicleType(typeName, hourlyRate);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle type added successfully.");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid hourly rate format.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add vehicle type: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the "Back" button is clicked.
     * Navigates the user back to the Super Admin Dashboard.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an error loading the Super Admin Dashboard scene.
     */
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/super_admin_dashboard.css").toExternalForm();
        SceneManager.loadScene("super_admin_dashboard.fxml", "Super Admin Dashboard", event, stylesheet);
    }

    /**
     * Displays an alert dialog with the specified type, title, and content.
     *
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert dialog.
     * @param content The message content of the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertUtil.showAlert(alertType, title, content);
    }

    /**
     * Clears the input fields on the form.
     */
    private void clearFields() {
        typeNameField.clear();
        hourlyRateField.clear();
    }
}