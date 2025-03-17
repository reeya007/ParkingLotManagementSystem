package com.parkinglot.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

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
    private TableColumn<User, Integer> roleColumn;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleId")); // Assuming roleId is an int, you can add conversion if needed

        try {
            List<User> userList = userService.getAllUsers();
            ObservableList<User> observableUsers = FXCollections.observableArrayList(userList);
            usersTable.setItems(observableUsers);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an alert)
        }
    }
}