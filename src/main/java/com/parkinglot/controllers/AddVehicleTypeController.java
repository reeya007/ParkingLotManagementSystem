package com.parkinglot.controllers;

import com.parkinglot.models.VehicleType;
import com.parkinglot.services.VehicleTypeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddVehicleTypeController {

    @FXML
    private TextField typeNameField;

    @FXML
    private TextField hourlyRateField;

    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();

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
            VehicleType vehicleType = new VehicleType();
            vehicleType.setTypeName(typeName);
            vehicleType.setHourlyRate(hourlyRate);

            vehicleTypeService.addVehicleType(vehicleType);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle type added successfully.");
            typeNameField.clear();
            hourlyRateField.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid hourly rate format.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add vehicle type: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        loadScene("super_admin_dashboard.fxml", "Super Admin Dashboard", event);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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