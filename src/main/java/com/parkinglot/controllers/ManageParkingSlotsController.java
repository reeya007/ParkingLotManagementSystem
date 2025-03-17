package com.parkinglot.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.services.ParkingSlotService;
import javafx.event.ActionEvent;

public class ManageParkingSlotsController {

    @FXML
    private TableView<ParkingSlot> parkingSlotsTable;

    @FXML
    private TableColumn<ParkingSlot, Integer> idColumn;

    @FXML
    private TableColumn<ParkingSlot, Integer> floorColumn;

    @FXML
    private TableColumn<ParkingSlot, String> rowColumn;

    @FXML
    private TableColumn<ParkingSlot, Integer> columnColumn;

    @FXML
    private TableColumn<ParkingSlot, String> labelColumn;

    @FXML
    private TableColumn<ParkingSlot, Boolean> availableColumn;

    @FXML
    private TableColumn<ParkingSlot, Integer> vehicleTypeColumn;

    private final ParkingSlotService parkingSlotService = new ParkingSlotService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        floorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
        rowColumn.setCellValueFactory(new PropertyValueFactory<>("rowNum"));
        columnColumn.setCellValueFactory(new PropertyValueFactory<>("columnNum"));
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("allowedVehicleTypeId"));

        try {
            List<ParkingSlot> slotList = parkingSlotService.getAllParkingSlots();
            ObservableList<ParkingSlot> observableSlots = FXCollections.observableArrayList(slotList);
            parkingSlotsTable.setItems(observableSlots);

            // Add listener for row selection
            parkingSlotsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    // Enable buttons when a row is selected
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    @FXML
    public void handleAssignSlot(ActionEvent event) {
        ParkingSlot selectedSlot = parkingSlotsTable.getSelectionModel().getSelectedItem();
        if (selectedSlot != null) {
            selectedSlot.setAvailable(false); // Assign the slot
            try {
                parkingSlotService.updateParkingSlot(selectedSlot);
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }
        }
    }

    @FXML
    public void handleFreeSlot(ActionEvent event) {
        ParkingSlot selectedSlot = parkingSlotsTable.getSelectionModel().getSelectedItem();
        if (selectedSlot != null) {
            selectedSlot.setAvailable(true); // Free the slot
            try {
                parkingSlotService.updateParkingSlot(selectedSlot);
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event);
    }

    private void refreshTable() throws SQLException {
        List<ParkingSlot> slotList = parkingSlotService.getAllParkingSlots();
        ObservableList<ParkingSlot> observableSlots = FXCollections.observableArrayList(slotList);
        parkingSlotsTable.setItems(observableSlots);
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