package com.parkinglot.controllers;

import com.parkinglot.models.VehicleType;
import com.parkinglot.services.UserService;
import com.parkinglot.services.VehicleService;
import com.parkinglot.services.VehicleTypeService;
import com.parkinglot.utils.AlertUtil;
import com.parkinglot.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the "Customer Registration" screen.
 * Handles user input for registering new customers and their initial vehicle.
 */
public class CustomerRegistrationController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField vehicleNumberField;
    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    private final UserService userService = new UserService();
    private final VehicleService vehicleService = new VehicleService();
    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();
    private List<VehicleType> vehicleTypesList; // To store vehicleTypes list

    /**
     * Initializes the controller. Loads the available vehicle types into the combo box.
     */
    @FXML
    public void initialize() {
        try {
            vehicleTypesList = vehicleTypeService.getAllVehicleTypes(); // Store the list
            ObservableList<String> vehicleTypeNames = FXCollections.observableArrayList(
                    vehicleTypesList.stream().map(VehicleType::getTypeName).collect(Collectors.toList())
            );
            vehicleTypeComboBox.setItems(vehicleTypeNames);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load vehicle types.");
        }
    }

    /**
     * Handles the action when the "Register Customer" button is clicked.
     * Retrieves user and vehicle input, validates it, and calls the UserService and VehicleService
     * to register the new customer and their initial vehicle.
     */
    @FXML
    private void registerCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String vehicleNumber = vehicleNumberField.getText();
        String selectedVehicleTypeName = vehicleTypeComboBox.getValue();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || vehicleNumber.isEmpty() || selectedVehicleTypeName == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            // Find the VehicleType object from the selected name
            VehicleType selectedVehicleType = vehicleTypesList.stream()
                    .filter(vt -> vt.getTypeName().equals(selectedVehicleTypeName))
                    .findFirst()
                    .orElse(null);

            if (selectedVehicleType == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected vehicle type not found.");
                return;
            }

            userService.registerCustomer(name, address, phone, email, password, vehicleNumber, selectedVehicleType.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer and vehicle registered successfully.");
            clearFields();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the specified type, title, and content.
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert dialog.
     * @param content The message content of the alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertUtil.showAlert(alertType, title, content);
    }

    /**
     * Clears the input fields on the form.
     */
    private void clearFields() {
        nameField.clear();
        addressField.clear();
        phoneField.clear();
        emailField.clear();
        passwordField.clear();
        vehicleNumberField.clear();
        vehicleTypeComboBox.setValue(null);
    }

    /**
     * Handles the action when the "Back" button is clicked.
     * Navigates the user back to the welcome screen.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an error loading the welcome scene.
     */
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        SceneManager.loadScene("welcome.fxml", "Parking Lot Management System", event, stylesheet);
    }
}