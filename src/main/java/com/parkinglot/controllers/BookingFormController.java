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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;

import static com.parkinglot.utils.SceneManager.loadScene;

public class BookingFormController implements UserAwareController {

    @FXML private ComboBox<String> bookingVehicleComboBox; // Changed to String
    @FXML private ComboBox<String> availableSlotsComboBox; // Changed to String for Label

    private final VehicleService vehicleService = new VehicleService();
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final TransactionService transactionService = new TransactionService();
    private final UserService userService = new UserService();
    private User currentUser;
    private List<Vehicle> userVehicles; // To store the list of user's vehicles
    private List<ParkingSlot> availableParkingSlots; // To store the list of available ParkingSlots

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        populateVehicleComboBox();
        // handleAdvanceBooking(); // Removed initial call, will be called on vehicle selection
    }

    @FXML
    public void initialize() {
        // Initialization logic
        bookingVehicleComboBox.setOnAction(this::handleVehicleSelection); // Attach event handler
    }

    @FXML
    public void handleVehicleSelection(ActionEvent event) {
        if (bookingVehicleComboBox.getValue() != null) {
            populateAvailableSlots();
        } else {
            availableSlotsComboBox.getItems().clear(); // Clear available slots if no vehicle selected
        }
    }

    private void populateAvailableSlots() {
        String selectedLicensePlate = bookingVehicleComboBox.getValue();
        if (selectedLicensePlate != null) {
            try {
                Vehicle selectedVehicle = userVehicles.stream()
                        .filter(v -> v.getLicensePlate().equals(selectedLicensePlate))
                        .findFirst()
                        .orElse(null);

                if (selectedVehicle != null) {
                    availableParkingSlots = parkingSlotService.getAllParkingSlots().stream()
                            .filter(slot -> slot.isIsAvailable() && slot.getAllowedVehicleTypeId() == selectedVehicle.getVehicleTypeId())
                            .collect(Collectors.toList());
                    if (availableSlotsComboBox != null) {
                        availableSlotsComboBox.setItems(FXCollections.observableArrayList(
                                availableParkingSlots.stream().map(ParkingSlot::getLabel).collect(Collectors.toList())
                        ));
                    }
                } else {
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Selected vehicle not found.");
                    availableSlotsComboBox.getItems().clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Unable to load available slots");
                availableSlotsComboBox.getItems().clear();
            }
        } else {
            availableSlotsComboBox.getItems().clear();
        }
    }

    @FXML
    public void handleBookSlot(ActionEvent event) throws SQLException {
        if (availableSlotsComboBox != null && availableSlotsComboBox.getValue() != null && bookingVehicleComboBox != null && bookingVehicleComboBox.getValue() != null) {
            String selectedSlotLabel = availableSlotsComboBox.getValue();
            String selectedLicensePlate = bookingVehicleComboBox.getValue();

            Vehicle selectedVehicle = userVehicles.stream()
                    .filter(v -> v.getLicensePlate().equals(selectedLicensePlate))
                    .findFirst()
                    .orElse(null);

            ParkingSlot selectedSlot = availableParkingSlots.stream()
                    .filter(slot -> slot.getLabel().equals(selectedSlotLabel))
                    .findFirst()
                    .orElse(null);

            if (selectedVehicle != null && selectedSlot != null) {
                // Check if the user already has an assigned slot
                if (currentUser.getAssignedSlotId() != null) {
                    // You might want to check the status of the transaction for that slot as well
                    AlertUtil.showAlert(Alert.AlertType.WARNING, "Warning", "You already have an active parking slot.");
                    return;
                }

                // Check for existing pending booking for the same user and vehicle
                boolean hasPendingBooking = transactionService.hasPendingTransaction(currentUser.getId(), selectedVehicle.getId());

                if (hasPendingBooking) {
                    AlertUtil.showAlert(Alert.AlertType.WARNING, "Warning", "You already have a pending booking for this vehicle.");
                    return; // Avoid creating a new booking
                }

                Transaction transaction = new Transaction();
                transaction.setUserId(currentUser.getId());
                transaction.setVehicleId(selectedVehicle.getId());
                transaction.setParkingSlotId(selectedSlot.getId());
                transaction.setEntryTime(Timestamp.valueOf(LocalDateTime.now()));
                transaction.setPaymentStatus("Pending");
                try {
                    transactionService.addTransaction(transaction);
                    selectedSlot.setAvailable(false);
                    parkingSlotService.updateParkingSlot(selectedSlot);

                    // Update the user's assignedSlotId
                    currentUser.setAssignedSlotId(selectedSlot.getId());
                    userService.updateUser(currentUser);

                    AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Successfully booked parking slot: " + selectedSlot.getLabel());
                } catch (SQLException e) {
                    e.printStackTrace();
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error while booking slot");
                }
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Selected vehicle or slot not found.");
            }

        } else {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Please fill up all the fields");
        }
    }

    private void populateVehicleComboBox() {
        if (bookingVehicleComboBox != null && currentUser != null) {
            try {
                userVehicles = vehicleService.getVehiclesByUser(currentUser.getId()); // Store the list of vehicles
                List<String> vehicleNumbers = userVehicles.stream()
                        .map(Vehicle::getLicensePlate)
                        .collect(Collectors.toList());
                bookingVehicleComboBox.setItems(FXCollections.observableArrayList(vehicleNumbers));
            } catch (SQLException e) {
                e.printStackTrace();
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error getting Vehicles");
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet = getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("customer_dashboard.fxml", "Customer Dashboard", event, adminStylesheet, currentUser);
    }
}