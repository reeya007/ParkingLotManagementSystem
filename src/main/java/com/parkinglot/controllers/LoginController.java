package com.parkinglot.controllers;

import com.parkinglot.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import com.parkinglot.models.User;
import com.parkinglot.services.UserService;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter email and password.");
            return;
        }

        try {
            User user = userService.getUserByEmail(email);
            if (user != null && verifyPassword(password, user.getPasswordHash())) {
                redirectToDashboard(user); // Pass the User object
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Incorrect email or password.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean verifyPassword(String password, String storedHash) {
        return Objects.equals(hashPassword(password), storedHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            return bytesToHex(encodedHash);
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

    private void redirectToDashboard(User user) throws IOException { // Take User object
        Stage stage = (Stage) emailField.getScene().getWindow();
        FXMLLoader loader;

        switch (user.getRoleId()) {
            case 1: // Admin
                loader = new FXMLLoader(getClass().getResource("/com/parkinglot/views/admin_dashboard.fxml"));
                stage.setTitle("Admin Dashboard");
                break;
            case 2: // Customer
                loader = new FXMLLoader(getClass().getResource("/com/parkinglot/views/customer_dashboard.fxml"));
                stage.setTitle("Customer Dashboard");
                break;
            case 3: // Super Admin
                loader = new FXMLLoader(getClass().getResource("/com/parkinglot/views/super_admin_dashboard.fxml"));
                stage.setTitle("Super Admin Dashboard");
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid role ID.");
                return;
        }

        Parent root = loader.load();

        if (user.getRoleId() == 2) { // If customer
            CustomerDashboardController controller = loader.getController();
            controller.setCurrentUser(user); // Set current user
        }

        String customerStylesheet = getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        String superAdminStylesheet = getClass().getResource("/com/parkinglot/styles/super_admin_dashboard.css").toExternalForm();
        String adminStylesheet = getClass().getResource("/com/parkinglot/styles/admin_dashboard.css").toExternalForm();

        Scene scene = new Scene(root);
        if (user.getRoleId() == 2) {
            scene.getStylesheets().add(customerStylesheet);
        } else if (user.getRoleId() == 3) {
            scene.getStylesheets().add(superAdminStylesheet);
        } else {
            scene.getStylesheets().add(adminStylesheet);
        }
        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/welcome.css").toExternalForm();
        SceneManager.loadScene("welcome.fxml", "Parking Lot Management System", event, stylesheet);
    }
}