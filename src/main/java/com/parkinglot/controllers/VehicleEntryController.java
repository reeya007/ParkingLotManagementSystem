package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.Transaction;
import com.parkinglot.models.User;
import com.parkinglot.models.Vehicle;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.TransactionService;
import com.parkinglot.services.UserService;
import com.parkinglot.services.VehicleService;
import com.parkinglot.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static com.parkinglot.utils.SceneManager.loadScene;

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

            // Check if there's already a pending transaction for this vehicle
            boolean hasPendingEntry = transactionService.hasPendingTransactionForVehicle(vehicle.getId());
            if (hasPendingEntry) {
                showAlert(Alert.AlertType.WARNING, "Warning", "This vehicle already has a pending entry.");
                licensePlateField.clear();
                return; // Do nothing if there's a pending entry
            }

            List<ParkingSlot> availableSlots = parkingSlotService.getAllParkingSlots().stream()
                    .filter(ParkingSlot::isIsAvailable)
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

            // Update the user's assignedSlotId
            User user = userService.getUserById(vehicle.getUserId());
            if (user != null) {
                user.setAssignedSlotId(assignedSlot.getId());
                userService.updateUser(user);
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle entered successfully. Assigned slot: " + assignedSlot.getLabel());
            licensePlateField.clear();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Vehicle entry failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/admin_dashboard.css").toExternalForm();
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event, adminStylesheet);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertUtil.showAlert(alertType, title, content);
    }

}