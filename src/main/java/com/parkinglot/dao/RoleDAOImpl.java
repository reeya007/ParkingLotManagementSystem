package com.parkinglot.dao;

import com.parkinglot.models.UserRole;
import com.parkinglot.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public List<UserRole> getAllRoles() throws SQLException {
        List<UserRole> roles = new ArrayList<>();
        String sql = "SELECT id, role_name FROM UserRoles";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UserRole role = new UserRole();
                role.setId(resultSet.getInt("id"));
                role.setRoleName(resultSet.getString("role_name"));
                roles.add(role);
            }

        } catch (SQLException e) {
            throw new SQLException("Error fetching all roles: " + e.getMessage(), e);
        }
        return roles;
    }

}