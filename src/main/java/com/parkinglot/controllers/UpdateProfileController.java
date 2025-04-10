package com.parkinglot.controllers;

import com.parkinglot.models.User;
import com.parkinglot.services.UserService;
import com.parkinglot.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

import static com.parkinglot.utils.SceneManager.loadScene;

public class UpdateProfileController  implements UserAwareController {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    private final UserService userService = new UserService();
    private User currentUser;

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        populateProfileFields();
    }

    @FXML
    public void initialize() {
        // Initialization logic
    }

    @FXML
    public void handleUpdateProfile(ActionEvent event) throws SQLException {
        if (nameField != null && addressField != null && phoneField != null && currentUser != null) {
          try {
            currentUser.setName(nameField.getText());
            currentUser.setAddress(addressField.getText());
            currentUser.setPhoneNumber(phoneField.getText());
            userService.updateUser(currentUser);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Profile Updated");
          } catch (SQLException e) {
              e.printStackTrace();
              AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error while updating Profile");
          }
        } else {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Please fill all the fields");
        }
    }

    private void populateProfileFields() {
        if (nameField != null && addressField != null && phoneField != null && currentUser != null) {
            nameField.setText(currentUser.getName());
            addressField.setText(currentUser.getAddress());
            phoneField.setText(currentUser.getPhoneNumber());
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String adminStylesheet= getClass().getResource("/com/parkinglot/styles/customer_dashboard.css").toExternalForm();
        loadScene("customer_dashboard.fxml", "Customer Dashboard", event, adminStylesheet, currentUser);
    }
}