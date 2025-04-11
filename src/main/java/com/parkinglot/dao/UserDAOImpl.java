package com.parkinglot.dao;

import com.parkinglot.models.User;
import com.parkinglot.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Users (name, address, phone_number, email, password_hash, role_id) VALUES (?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getAddress());
            preparedStatement.setString(3, user.getPhoneNumber());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPasswordHash());
            preparedStatement.setInt(6, user.getRoleId());

            preparedStatement.executeUpdate();
        }
    }
    @Override
    public User getUserByEmail(String email) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, name, address, phone_number, email, password_hash, role_id, assigned_slot_id FROM Users WHERE email = ?")) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("email"),
                        resultSet.getString("password_hash"),
                        resultSet.getInt("role_id"),
                        resultSet.getObject("assigned_slot_id", Integer.class)
                );
            }
            return null;
        }
    }


    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, name, address, phone_number, email, password_hash, role_id, assigned_slot_id FROM Users")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("email"),
                        resultSet.getString("password_hash"),
                        resultSet.getInt("role_id"),
                        resultSet.getObject("assigned_slot_id", Integer.class)
                ));
            }
        }
        return users;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET name = ?, address = ?, phone_number = ?, assigned_slot_id = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getAddress());
            preparedStatement.setString(3, user.getPhoneNumber());

            Integer assignedSlotId = user.getAssignedSlotId();
            if (assignedSlotId != null) {
                preparedStatement.setInt(4, assignedSlotId);
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }

            preparedStatement.setInt(5, user.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(int id) throws SQLException {
        User user = null;
        String sql = "SELECT id, name, address, phone_number, email, password_hash, role_id, assigned_slot_id FROM Users WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setAddress(resultSet.getString("address"));
                    user.setPhoneNumber(resultSet.getString("phone_number"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setRoleId(resultSet.getInt("role_id"));
                    user.setAssignedSlotId((Integer) resultSet.getObject("assigned_slot_id")); // Handle nullable Integer
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting user by ID: " + e.getMessage(), e);
        }
        return user;
    }
}