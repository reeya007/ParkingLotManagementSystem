package com.parkinglot.controllers;

import com.parkinglot.models.User;
import com.parkinglot.services.UserService;
import com.parkinglot.utils.AlertUtil;
import com.parkinglot.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the "Admin Registration" screen.
 * Handles user input for registering new admin users.
 */
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

    /**
     * Handles the action when the "Register Admin" button is clicked.
     * Retrieves user input, validates it, and calls the UserService to register the new admin user.
     */
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

        try {
            User registeredUser = userService.registerAdmin(name, address, phone, email, password);
            if (registeredUser != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Admin registered successfully.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Admin registration failed.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the specified type, title, and content.
     *
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
    }

    /**
     * Handles the action when the "Back" button is clicked.
     * Navigates the user back to the Super Admin Dashboard.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an error loading the Super Admin Dashboard scene.
     */
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String superAdminStylesheet = getClass().getResource("/com/parkinglot/styles/super_admin_dashboard.css").toExternalForm();
        SceneManager.loadScene("super_admin_dashboard.fxml", "Super Admin Dashboard", event, superAdminStylesheet);
    }
}