package com.parkinglot.controllers;

import com.parkinglot.models.Transaction;
import com.parkinglot.models.User;
import com.parkinglot.services.TransactionService;
import com.parkinglot.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static com.parkinglot.utils.SceneManager.loadScene;

public class TransactionHistoryController implements UserAwareController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, Timestamp> entryTimeColumn;
    @FXML private TableColumn<Transaction, Timestamp> exitTimeColumn;
    @FXML private TableColumn<Transaction, Integer> durationColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> statusColumn;

    private final TransactionService transactionService = new TransactionService();
    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        populateTransactionsTable();
    }

    @FXML
    public void initialize() {

    }

    private void populateTransactionsTable() {
        if (transactionsTable != null && currentUser != null) {
            try {
                List<Transaction> transactions = transactionService.getAllTransactions().stream().filter(t -> t.getUserId() == currentUser.getId()).toList();
                ObservableList<Transaction> transactionList = FXCollections.observableArrayList(transactions);
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                entryTimeColumn.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
                exitTimeColumn.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
                durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
                amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
                transactionsTable.setItems(transactionList);
            } catch (SQLException e) {
                e.printStackTrace();
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error while getting Transaction Details");
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("customer_dashboard.fxml", "Customer Dashboard", event, adminStylesheet, currentUser);
    }
}