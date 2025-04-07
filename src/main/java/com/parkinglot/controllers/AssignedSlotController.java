package com.parkinglot.controllers;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.User;
import com.parkinglot.services.ParkingSlotService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

import static com.parkinglot.utils.SceneManager.loadScene;

public class AssignedSlotController implements UserAwareController{

    @FXML private Label assignedSlotLabel;
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        populateAssignedSlot();
    }

    @FXML
    public void initialize() {
        // Initialization logic
    }

    private void populateAssignedSlot() {
        if (assignedSlotLabel != null && currentUser != null) {
            if (currentUser.getAssignedSlotId() != null) {
                try {
                    ParkingSlot slot = parkingSlotService.getParkingSlotById(currentUser.getAssignedSlotId());
                    assignedSlotLabel.setText("Assigned Slot: " + slot.getLabel());
                } catch (SQLException e) {
                    e.printStackTrace();
                    assignedSlotLabel.setText("Error loading assigned slot.");
                }
            } else {
                assignedSlotLabel.setText("No assigned slot.");
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("customer_dashboard.fxml", "Customer Dashboard", event, adminStylesheet, currentUser);
    }
}