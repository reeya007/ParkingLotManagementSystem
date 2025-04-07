package com.parkinglot.controllers;

import com.parkinglot.models.Vehicle;
import com.parkinglot.models.VehicleType;
import com.parkinglot.services.VehicleService;
import com.parkinglot.services.VehicleTypeService;
import com.parkinglot.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.parkinglot.models.User;
import com.parkinglot.services.UserService;

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

    @FXML
    private void registerCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setAddress(address);
        user.setPhoneNumber(phone);
        user.setEmail(email);
        user.setPasswordHash(hashPassword(password));
        user.setRoleId(2); // 2 is the role_id for Customer

        try {
            user = userService.registerUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer registered successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }

        registerVehicle(user);
    }

    private void registerVehicle(User user) {
        String vehicleNumber = vehicleNumberField.getText();
        String selectedVehicleTypeName = vehicleTypeComboBox.getValue();

        if (vehicleNumber.isEmpty() || selectedVehicleTypeName == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter vehicle number and select vehicle type.");
            return;
        }

        // Find the VehicleType object from the selected name
        VehicleType selectedVehicleType = vehicleTypesList.stream()
                .filter(vt -> vt.getTypeName().equals(selectedVehicleTypeName))
                .findFirst()
                .orElse(null);

        if (selectedVehicleType == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Selected vehicle type not found.");
            return;
        }

        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setUserId(user.getId());
            vehicle.setLicensePlate(vehicleNumber);
            vehicle.setVehicleTypeId(selectedVehicleType.getId());

            vehicleService.addVehicle(vehicle);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle registered successfully.");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        nameField.clear();
        addressField.clear();
        phoneField.clear();
        emailField.clear();
        passwordField.clear();
        vehicleNumberField.clear();
        vehicleTypeComboBox.setValue(null);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/login.css").toExternalForm();
        SceneManager.loadScene("welcome.fxml", "Parking Lot Management System", event, stylesheet);
    }
}