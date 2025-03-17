package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.VehicleType;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.VehicleTypeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AddParkingSlotController {

    @FXML
    private TextField floorField;
    @FXML
    private TextField rowsField;
    @FXML
    private TextField columnsField;
    @FXML
    private ComboBox<VehicleType> vehicleTypeComboBox;

    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();

    @FXML
    public void initialize() {
        try {
            List<VehicleType> vehicleTypes = vehicleTypeService.getAllVehicleTypes();
            ObservableList<VehicleType> observableVehicleTypes = FXCollections.observableArrayList(vehicleTypes);
            vehicleTypeComboBox.setItems(observableVehicleTypes);
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
        VehicleType selectedVehicleType = vehicleTypeComboBox.getValue();

        if (floorStr.isEmpty() || rowsStr.isEmpty() || columnsStr.isEmpty() || selectedVehicleType == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            int floor = Integer.parseInt(floorStr);
            int rows = Integer.parseInt(rowsStr);
            int columns = Integer.parseInt(columnsStr);

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
        loadScene("super_admin_dashboard.fxml", "Super Admin Dashboard", event);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        floorField.clear();
        rowsField.clear();
        columnsField.clear();
        vehicleTypeComboBox.setValue(null);
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