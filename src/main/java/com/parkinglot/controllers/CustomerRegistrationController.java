package com.parkinglot.controllers;

import com.parkinglot.models.Vehicle;
import com.parkinglot.models.VehicleType;
import com.parkinglot.services.VehicleService;
import com.parkinglot.services.VehicleTypeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

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
    private ComboBox<VehicleType> vehicleTypeComboBox;

    private final UserService userService = new UserService();
    private final VehicleService vehicleService = new VehicleService();
    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();

    @FXML
    public void initialize() {
        try {
            List<VehicleType> vehicleTypes = vehicleTypeService.getAllVehicleTypes();
            ObservableList<VehicleType> observableVehicleTypes = FXCollections.observableArrayList(vehicleTypes);
            vehicleTypeComboBox.setItems(observableVehicleTypes);
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
            userService.registerUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer registered successfully.");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }

        registerVehicle(user);
    }

    private void registerVehicle(User user) {
        String vehicleNumber = vehicleNumberField.getText();
        VehicleType selectedVehicleType = vehicleTypeComboBox.getValue();

        if (vehicleNumber.isEmpty() || selectedVehicleType == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter vehicle number and select vehicle type.");
            return;
        }

        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setUserId(user.getId()); // Assuming you set the user ID after adding to the database
            vehicle.setLicensePlate(vehicleNumber);
            vehicle.setVehicleTypeId(selectedVehicleType.getId());

            vehicleService.addVehicle(vehicle);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer and vehicle registered successfully.");
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
}