package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.Transaction;
import com.parkinglot.models.User;
import com.parkinglot.models.Vehicle;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.TransactionService;
import com.parkinglot.services.VehicleService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.event.ActionEvent;

import static com.parkinglot.utils.SceneManager.loadScene;

public class BookingFormController implements UserAwareController{

    @FXML private DatePicker bookingDatePicker;
    @FXML private TextField bookingTimeField;
    @FXML private ComboBox<Vehicle> bookingVehicleComboBox;
    @FXML private ComboBox<ParkingSlot> availableSlotsComboBox;

    private final VehicleService vehicleService = new VehicleService();
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final TransactionService transactionService = new TransactionService();
    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        populateVehicleComboBox();
        handleAdvanceBooking(); // Initial population if data exists
    }

    @FXML
    public void initialize() {
        // Initialization logic
    }

    @FXML
    public void handleAdvanceBooking() {
        if (bookingDatePicker != null && bookingTimeField != null && bookingVehicleComboBox != null && bookingDatePicker.getValue() != null && !bookingTimeField.getText().isEmpty() && bookingVehicleComboBox.getValue() != null) {
            try {
                LocalDateTime bookingDateTime = LocalDateTime.parse(bookingDatePicker.getValue() + "T" + bookingTimeField.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                Timestamp bookingTimestamp = Timestamp.valueOf(bookingDateTime);
                Vehicle selectedVehicle = bookingVehicleComboBox.getValue();
                List<ParkingSlot> availableSlots = parkingSlotService.getAllParkingSlots().stream().filter(slot -> slot.isIsAvailable() && slot.getAllowedVehicleTypeId() == selectedVehicle.getVehicleTypeId()).toList();
                if (availableSlotsComboBox != null) {
                    availableSlotsComboBox.setItems(FXCollections.observableArrayList(availableSlots));
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle parsing or SQL errors
            }
        }
    }

    @FXML
    public void handleBookSlot(ActionEvent event) throws SQLException {
        if (availableSlotsComboBox != null && availableSlotsComboBox.getValue() != null && bookingVehicleComboBox != null && bookingVehicleComboBox.getValue() != null) {
            ParkingSlot selectedSlot = availableSlotsComboBox.getValue();
            Transaction transaction = new Transaction();
            transaction.setUserId(currentUser.getId());
            transaction.setVehicleId(bookingVehicleComboBox.getValue().getId());
            transaction.setParkingSlotId(selectedSlot.getId());
            transaction.setEntryTime(Timestamp.valueOf(LocalDateTime.now()));
            transaction.setPaymentStatus("Pending");
            transactionService.addTransaction(transaction);
            selectedSlot.setAvailable(false);
            parkingSlotService.updateParkingSlot(selectedSlot);
            // Optionally provide feedback or update UI
        }
    }

    private void populateVehicleComboBox() {
        if (bookingVehicleComboBox != null && currentUser != null) {
            try {
                List<Vehicle> vehicles = vehicleService.getVehiclesByUser(currentUser.getId());
                bookingVehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL error
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("customer_dashboard.fxml", "Customer Dashboard", event, adminStylesheet, currentUser);
    }
}