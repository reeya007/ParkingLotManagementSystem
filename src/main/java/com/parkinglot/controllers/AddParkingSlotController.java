package com.parkinglot.controllers;

import com.parkinglot.models.VehicleType;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.VehicleTypeService;
import com.parkinglot.utils.AlertUtil;
import com.parkinglot.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AddParkingSlotController {

    @FXML
    private TextField floorField;
    @FXML
    private TextField rowsField;
    @FXML
    private TextField columnsField;
    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();
    private List<VehicleType> vehicleTypesList; // To store VehicleType objects

    @FXML
    public void initialize() {
        try {
            vehicleTypesList = vehicleTypeService.getAllVehicleTypes(); // Fetch and store the list
            ObservableList<String> vehicleTypeNames = FXCollections.observableArrayList(
                    vehicleTypesList.stream().map(VehicleType::getTypeName).collect(Collectors.toList())
            );
            vehicleTypeComboBox.setItems(vehicleTypeNames);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load vehicle types.");
        }
    }

    @FXML
    public void handleAddParkingSlots() {
        String floorStr = floorField.getText();
        String rowsStr = rowsField.getText();
        String columnsStr = columnsField.getText();
        String selectedVehicleTypeName = vehicleTypeComboBox.getValue();

        if (floorStr.isEmpty() || rowsStr.isEmpty() || columnsStr.isEmpty() || selectedVehicleTypeName == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            int floor = Integer.parseInt(floorStr);
            int rows = Integer.parseInt(rowsStr);
            int columns = Integer.parseInt(columnsStr);

            // Find the VehicleType object from the selected name
            VehicleType selectedVehicleType = vehicleTypesList.stream()
                    .filter(vt -> vt.getTypeName().equals(selectedVehicleTypeName))
                    .findFirst()
                    .orElse(null);

            if (selectedVehicleType == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected vehicle type not found.");
                return;
            }

            parkingSlotService.addParkingSlotsInGrid(floor, rows, columns, selectedVehicleType);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Parking slots added successfully.");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid floor, rows, or columns format.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add parking slots: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String superAdminStylesheet = getClass().getResource("/com/parkinglot/styles/super_admin_dashboard.css").toExternalForm();
        SceneManager.loadScene("super_admin_dashboard.fxml", "Super Admin Dashboard", event, superAdminStylesheet);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertUtil.showAlert(alertType, title, content);
    }

    private void clearFields() {
        floorField.clear();
        rowsField.clear();
        columnsField.clear();
        vehicleTypeComboBox.setValue(null);
    }
}