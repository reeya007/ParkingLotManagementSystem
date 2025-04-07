package com.parkinglot.controllers;



import com.parkinglot.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

import static com.parkinglot.utils.SceneManager.loadScene;



public class CustomerDashboardController implements UserAwareController {

    private User currentUser;


    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void loadTransactionHistoryView(ActionEvent event) throws IOException {
      String  styles = getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("transaction_history_view.fxml", "Transaction History", event, styles, currentUser);
    }

    public void loadBookingFormView(ActionEvent event) throws IOException {
        String styles = getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("booking_form_view.fxml", "Book Slot", event, styles, currentUser);
    }

    public void loadAssignedSlotView(ActionEvent event) throws IOException {
        String styles = getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("assigned_slot_view.fxml", "Assigned Slot", event, styles, currentUser);
    }

    public void loadVehicleInfoView(ActionEvent event) throws IOException {
        String styles = getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("vehicle_info_view.fxml", "Vehicle Info", event, styles, currentUser);
    }

    public void loadUpdateProfileView(ActionEvent event) throws IOException {
        String styles = getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("update_profile_view.fxml", "Update Profile", event, styles, currentUser);
    }

    @FXML public void handleLogout(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        loadScene("welcome.fxml", "Parking Lot Management System", event, adminStylesheet);
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        loadScene("welcome.fxml", "Parking Lot Management System", event, adminStylesheet);
    }

}