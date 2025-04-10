package com.parkinglot.controllers;

import com.parkinglot.models.UserRole;
import com.parkinglot.services.UserRoleService;
import com.parkinglot.utils.AlertUtil;
import com.parkinglot.utils.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.parkinglot.models.User;
import com.parkinglot.services.UserService;

public class ViewUsersController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> roleColumn; // Changed to String

    private final UserService userService = new UserService();
    private final UserRoleService roleService = new UserRoleService();
    private Map<Integer, String> roleNameMap;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        // Set a Callback for the roleColumn to display the role name
        roleColumn.setCellValueFactory(cellData -> {
            Integer roleId = cellData.getValue().getRoleId();
            String roleName = roleNameMap.getOrDefault(roleId, "Unknown");
            return new SimpleStringProperty(roleName);
        });

        loadUserData();
    }

    private void loadUserData() {
        try {
            // Fetch all roles and create a map of role ID to role name
            List<UserRole> roleList = roleService.getAllRoles();
            roleNameMap = roleList.stream().collect(Collectors.toMap(UserRole::getId, UserRole::getRoleName));

            // Fetch all users
            List<User> userList = userService.getAllUsers();
            ObservableList<User> observableUsers = FXCollections.observableArrayList(userList);
            usersTable.setItems(observableUsers);
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error while loading Users");
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        String stylesheet = getClass().getResource("/com/parkinglot/styles/super_admin_dashboard.css").toExternalForm();
        SceneManager.loadScene("super_admin_dashboard.fxml", "Super Admin Dashboard", event, stylesheet);
    }
}