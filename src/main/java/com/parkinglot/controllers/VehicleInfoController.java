package com.parkinglot.controllers;

import com.parkinglot.models.User;
import com.parkinglot.models.Vehicle;
import com.parkinglot.models.VehicleType;
import com.parkinglot.services.VehicleService;
import com.parkinglot.services.VehicleTypeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.parkinglot.utils.SceneManager.loadScene;

public class VehicleInfoController implements UserAwareController {

    @FXML private TableView<Vehicle> vehiclesTable;
    @FXML private TableColumn<Vehicle, String> licensePlateColumn;
    @FXML private TableColumn<Vehicle, String> vehicleTypeIdColumn;

    private final VehicleService vehicleService = new VehicleService();
    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();
    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        populateVehicleTable();
    }

    @FXML
    public void initialize() {
        // Initialization logic
    }

    private void populateVehicleTable() {
        if (vehiclesTable != null && currentUser != null) {
            try {
                List<Vehicle> vehicles = vehicleService.getVehiclesByUser(currentUser.getId());
                Map<Integer, String> vehicleTypeMap = vehicleTypeService.getAllVehicleTypes().stream()
                        .collect(Collectors.toMap(VehicleType::getId, VehicleType::getTypeName));

                ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList(vehicles);

                licensePlateColumn.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
                vehicleTypeIdColumn.setCellValueFactory(cellData -> {
                    int vehicleTypeId = cellData.getValue().getVehicleTypeId();
                    return new SimpleStringProperty(vehicleTypeMap.getOrDefault(vehicleTypeId, "Unknown"));
                });

                vehiclesTable.setItems(vehicleList);
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