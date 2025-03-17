package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.Transaction;
import com.parkinglot.models.Vehicle;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.TransactionService;
import com.parkinglot.services.UserService;
import com.parkinglot.services.VehicleService;
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
import java.sql.Timestamp;
import java.util.List;

public class VehicleEntryController {

    @FXML
    private TextField licensePlateField;

    private final VehicleService vehicleService = new VehicleService();
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final TransactionService transactionService = new TransactionService();
    private final UserService userService = new UserService();

    @FXML
    public void handleVehicleEntry() {
        String licensePlate = licensePlateField.getText();

        try {
            Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
            if (vehicle == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Vehicle not found.");
                return;
            }

            List<ParkingSlot> availableSlots = parkingSlotService.getAllParkingSlots().stream()
                    .filter(ParkingSlot::isAvailable)
                    .filter(slot -> slot.getAllowedVehicleTypeId() == vehicle.getVehicleTypeId())
                    .toList();

            if (availableSlots.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "No available parking slots for this vehicle type.");
                return;
            }

            ParkingSlot assignedSlot = availableSlots.getFirst();
            assignedSlot.setAvailable(false);
            parkingSlotService.updateParkingSlot(assignedSlot);

            Transaction transaction = new Transaction();
            transaction.setUserId(vehicle.getUserId());
            transaction.setVehicleId(vehicle.getId());
            transaction.setParkingSlotId(assignedSlot.getId());
            transaction.setEntryTime(new Timestamp(System.currentTimeMillis()));
            transaction.setPaymentStatus("Pending");

            transactionService.addTransaction(transaction);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle entered successfully. Assigned slot: " + assignedSlot.getLabel());
            licensePlateField.clear();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Vehicle entry failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event);
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