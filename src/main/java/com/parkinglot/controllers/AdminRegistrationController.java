package com.parkinglot.controllers;

import com.parkinglot.utils.AlertUtil;
import com.parkinglot.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.parkinglot.utils.DatabaseUtil;

import com.parkinglot.models.User;
import com.parkinglot.services.UserService;

public class AdminRegistrationController {

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

    private final UserService userService = new UserService();

    @FXML
    private void registerAdmin() {
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
        user.setRoleId(1); // 1 is the role_id for Admin

        try {
            user = userService.registerUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Admin registered successfully.");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertUtil.showAlert(alertType, title, content);
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

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String superAdminStylesheet = getClass().getResource("/com/parkinglot/styles/super_admin_dashboard.css").toExternalForm();
        SceneManager.loadScene("super_admin_dashboard.fxml", "Super Admin Dashboard", event, superAdminStylesheet);
    }
}