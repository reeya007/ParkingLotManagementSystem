package com.parkinglot.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.services.ParkingSlotService;
import static com.parkinglot.utils.SceneManager.loadScene;

public class ViewParkingSlotsController {

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
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("allowedVehicleTypeId"));

        // Set the cell value factory to use the property itself.
        availableColumn.setCellValueFactory(cellData -> cellData.getValue().isAvailableProperty());

        try {
            List<ParkingSlot> slotList = parkingSlotService.getAllParkingSlots();
            ObservableList<ParkingSlot> observableSlots = FXCollections.observableArrayList(slotList);
            parkingSlotsTable.setItems(observableSlots);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet = getClass().getResource("/com/parkinglot/styles/admin_dashboard.css").toExternalForm();
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event, adminStylesheet);
    }
}