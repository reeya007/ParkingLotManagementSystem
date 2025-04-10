package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.Transaction;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.TransactionService;
import com.parkinglot.utils.AlertUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.parkinglot.utils.SceneManager.loadScene;

public class MonitorTransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, Integer> userIdColumn;

    @FXML
    private TableColumn<Transaction, Integer> vehicleIdColumn;

    @FXML
    private TableColumn<Transaction, String> slotIdColumn; // Changed to String for Label

    @FXML
    private TableColumn<Transaction, Timestamp> entryTimeColumn;

    @FXML
    private TableColumn<Transaction, Timestamp> exitTimeColumn;

    @FXML
    private TableColumn<Transaction, Integer> durationColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, String> statusColumn;

    private final TransactionService transactionService = new TransactionService();
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private Map<Integer, String> parkingSlotLabelMap;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        // Set the cell value factory for slotIdColumn to display the label
        slotIdColumn.setCellValueFactory(cellData -> {
            Integer slotId = cellData.getValue().getParkingSlotId();
            String slotLabel = parkingSlotLabelMap.getOrDefault(slotId, "Unknown");
            return new SimpleStringProperty(slotLabel);
        });
        entryTimeColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        exitTimeColumn.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        loadTransactionsData();
    }

    private void loadTransactionsData() {
        try {
            // Fetch all parking slots and create a map of ID to label
            List<ParkingSlot> parkingSlotList = parkingSlotService.getAllParkingSlots();
            parkingSlotLabelMap = parkingSlotList.stream().collect(Collectors.toMap(ParkingSlot::getId, ParkingSlot::getLabel));

            // Fetch all transactions
            List<Transaction> transactionList = transactionService.getAllTransactions();
            ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactionList);
            transactionsTable.setItems(observableTransactions);
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error while loading Transaction Data");
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet = getClass().getResource("/com/parkinglot/styles/admin_dashboard.css").toExternalForm();
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event, adminStylesheet);
    }
}