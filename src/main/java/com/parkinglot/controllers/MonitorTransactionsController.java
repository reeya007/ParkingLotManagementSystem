package com.parkinglot.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.parkinglot.models.Transaction;
import com.parkinglot.services.TransactionService;
import javafx.event.ActionEvent;

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
    private TableColumn<Transaction, Integer> slotIdColumn;

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

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        slotIdColumn.setCellValueFactory(new PropertyValueFactory<>("parkingSlotId"));
        entryTimeColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        exitTimeColumn.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        try {
            List<Transaction> transactionList = transactionService.getAllTransactions();
            ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactionList);
            transactionsTable.setItems(observableTransactions);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/admin_dashboard.css").toExternalForm();
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event, adminStylesheet);
    }

}