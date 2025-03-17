package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.Transaction;
import com.parkinglot.models.Vehicle;
import com.parkinglot.models.VehicleType;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.TransactionService;
import com.parkinglot.services.VehicleService;
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
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class VehicleExitController {

    @FXML
    private TextField licensePlateField;

    private final VehicleService vehicleService = new VehicleService();
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void handleVehicleExit() {
        String licensePlate = licensePlateField.getText();

        try {
            Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
            if (vehicle == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Vehicle not found.");
                return;
            }

            List<Transaction> activeTransactions = transactionService.getAllTransactions().stream()
                    .filter(t -> t.getVehicleId() == vehicle.getId())
                    .filter(t -> t.getExitTime() == null)
                    .toList();

            if (activeTransactions.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "No active transactions found for this vehicle.");
                return;
            }

            Transaction transaction = activeTransactions.getFirst();
            ParkingSlot assignedSlot = parkingSlotService.getParkingSlotById(transaction.getParkingSlotId());
            assignedSlot.setAvailable(true);
            parkingSlotService.updateParkingSlot(assignedSlot);

            Timestamp exitTime = new Timestamp(System.currentTimeMillis());
            transaction.setExitTime(exitTime);

            LocalDateTime entryDateTime = transaction.getEntryTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime exitDateTime = exitTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Duration duration = Duration.between(entryDateTime, exitDateTime);
            int durationMinutes = (int) duration.toMinutes();
            transaction.setDurationMinutes(durationMinutes);

            double amount = calculateParkingFee(durationMinutes, vehicle.getVehicleTypeId()); // Implement fee calculation logic
            transaction.setAmount(amount);
            transaction.setPaymentStatus("Completed");

            transactionService.updateTransaction(transaction);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle exited successfully. Total amount: $" + amount);
            licensePlateField.clear();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Vehicle exit failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double calculateParkingFee(int durationMinutes, int vehicleTypeId) throws SQLException {
        VehicleTypeService vehicleTypeService = new VehicleTypeService();
        VehicleType vehicleType = vehicleTypeService.getAllVehicleTypes().stream()
                .filter(vt -> vt.getId() == vehicleTypeId)
                .findFirst()
                .orElse(null);

        if (vehicleType == null) {
            return 0.0; // Or handle the error appropriately
        }

        return (durationMinutes / 60.0) * vehicleType.getHourlyRate();
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