package com.parkinglot.controllers;

import com.parkinglot.models.*;
import com.parkinglot.services.*;
import com.parkinglot.utils.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerDashboardController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, Timestamp> entryTimeColumn;
    @FXML private TableColumn<Transaction, Timestamp> exitTimeColumn;
    @FXML private TableColumn<Transaction, Integer> durationColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> statusColumn;
    @FXML private DatePicker bookingDatePicker;
    @FXML private TextField bookingTimeField;
    @FXML private ComboBox<Vehicle> bookingVehicleComboBox;
    @FXML private ComboBox<ParkingSlot> availableSlotsComboBox;
    @FXML private Label assignedSlotLabel;
    @FXML private TableView<Vehicle> vehiclesTable;
    @FXML private TableColumn<Vehicle, String> licensePlateColumn;
    @FXML private TableColumn<Vehicle, String> vehicleTypeIdColumn;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();
    private final TransactionService transactionService = new TransactionService();
    private final VehicleService vehicleService = new VehicleService();
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final UserService userService = new UserService();
    private User currentUser;

    @FXML
    public void initialize() {
        System.out.println(getClass().getResource("/com/parkinglot/styles/customer_dashboard.css"));
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        try {
            populateTransactionsTable(); // Directly populate transaction table
            populateVehicleComboBox();
            populateVehicleTable(); // Directly populate vehicle table
            populateAssignedSlot();
            handleAdvanceBooking();
            populateProfileFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTransactionsTable() throws SQLException {
        List<Transaction> transactions = transactionService.getAllTransactions().stream().filter(t -> t.getUserId() == currentUser.getId()).toList();
        ObservableList<Transaction> transactionList = FXCollections.observableArrayList(transactions);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        entryTimeColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        exitTimeColumn.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        transactionsTable.setItems(transactionList);
    }

    @FXML public void handleAdvanceBooking() throws SQLException {
        if (bookingDatePicker.getValue() != null && !bookingTimeField.getText().isEmpty() && bookingVehicleComboBox.getValue() != null) {
            LocalDateTime bookingDateTime = LocalDateTime.parse(bookingDatePicker.getValue() + "T" + bookingTimeField.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Timestamp bookingTimestamp = Timestamp.valueOf(bookingDateTime);
            Vehicle selectedVehicle = bookingVehicleComboBox.getValue();
            List<ParkingSlot> availableSlots = parkingSlotService.getAllParkingSlots().stream().filter(slot -> slot.isIsAvailable() && slot.getAllowedVehicleTypeId() == selectedVehicle.getVehicleTypeId()).toList();
            availableSlotsComboBox.setItems(FXCollections.observableArrayList(availableSlots));
        }
    }

    @FXML public void handleBookSlot(ActionEvent event) throws SQLException {
        if (availableSlotsComboBox.getValue() != null) {
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
            populateTransactionsTable();
        }
    }

    @FXML public void handleViewAssignedSlot() throws SQLException {
        populateAssignedSlot();
    }

    @FXML public void handleUpdateProfile(ActionEvent event) throws SQLException {
        currentUser.setName(nameField.getText());
        currentUser.setAddress(addressField.getText());
        currentUser.setPhoneNumber(phoneField.getText());
        userService.registerUser(currentUser);
    }

    @FXML public void handleLogout(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        SceneManager.loadScene("welcome.fxml", "Welcome", event, adminStylesheet);
    }

    @FXML public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        SceneManager.loadScene("welcome.fxml", "Welcome", event, adminStylesheet);
    }

    private void populateVehicleComboBox() throws SQLException {
        List<Vehicle> vehicles = vehicleService.getVehiclesByUser(currentUser.getId());
        bookingVehicleComboBox.setItems(FXCollections.observableArrayList(vehicles));
    }

    private void populateVehicleTable() throws SQLException {
        List<Vehicle> vehicles = vehicleService.getVehiclesByUser(currentUser.getId());
        Map<Integer, String> vehicleTypeMap = vehicleTypeService.getAllVehicleTypes().stream()
                .collect(Collectors.toMap(VehicleType::getId, VehicleType::getTypeName));

        ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList(vehicles);

        licensePlateColumn.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        vehicleTypeIdColumn.setCellValueFactory(cellData -> {
            int vehicleTypeId = cellData.getValue().getVehicleTypeId();
            return new SimpleStringProperty(vehicleTypeMap.getOrDefault(vehicleTypeId, "Unknown")); // Use "Unknown" if not found
        });

        vehiclesTable.setItems(vehicleList);
    }

    private void populateAssignedSlot() throws SQLException {
        if (currentUser.getAssignedSlotId() != null) {
            ParkingSlot slot = parkingSlotService.getParkingSlotById(currentUser.getAssignedSlotId());
            assignedSlotLabel.setText("Assigned Slot: " + slot.getLabel());
        } else {
            assignedSlotLabel.setText("No assigned slot.");
        }
    }

    private void populateProfileFields() {
        if (currentUser != null) {
            nameField.setText(currentUser.getName());
            addressField.setText(currentUser.getAddress());
            phoneField.setText(currentUser.getPhoneNumber());
        }
    }
}