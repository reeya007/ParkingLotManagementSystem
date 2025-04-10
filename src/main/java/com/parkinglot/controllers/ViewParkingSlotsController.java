package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.VehicleType;
import com.parkinglot.services.ParkingSlotService;
import com.parkinglot.services.VehicleTypeService;
import com.parkinglot.utils.AlertUtil;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private TableColumn<ParkingSlot, String> vehicleTypeColumn;

    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();
    private Map<Integer, String> vehicleTypeNameMap;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        floorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
        rowColumn.setCellValueFactory(new PropertyValueFactory<>("rowNum"));
        columnColumn.setCellValueFactory(new PropertyValueFactory<>("columnNum"));
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        availableColumn.setCellValueFactory(cellData -> cellData.getValue().isAvailableProperty());

        // Set the cell value factory for vehicle type to display the name
        vehicleTypeColumn.setCellValueFactory(cellData -> {
            Integer vehicleTypeId = cellData.getValue().getAllowedVehicleTypeId();
            String vehicleTypeName = vehicleTypeNameMap.getOrDefault(vehicleTypeId, "Unknown");
            return new SimpleStringProperty(vehicleTypeName);
        });

        loadParkingSlotsData();
    }

    private void loadParkingSlotsData() {
        try {
            // Fetch all vehicle types and create a map of ID to name
            List<VehicleType> vehicleTypeList = vehicleTypeService.getAllVehicleTypes();
            vehicleTypeNameMap = vehicleTypeList.stream()
                    .collect(Collectors.toMap(VehicleType::getId, VehicleType::getTypeName));

            // Fetch all parking slots
            List<ParkingSlot> slotList = parkingSlotService.getAllParkingSlots();
            ObservableList<ParkingSlot> observableSlots = FXCollections.observableArrayList(slotList);
            parkingSlotsTable.setItems(observableSlots);
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error while loading Parking Slots");
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet = getClass().getResource("/com/parkinglot/styles/admin_dashboard.css").toExternalForm();
        loadScene("admin_dashboard.fxml", "Admin Dashboard", event, adminStylesheet);
    }
}