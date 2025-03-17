package com.parkinglot.dao;

import com.parkinglot.models.VehicleType;
import com.parkinglot.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleTypeDAOImpl implements VehicleTypeDAO {

    @Override
    public List<VehicleType> getAllVehicleTypes() throws SQLException {
        List<VehicleType> types = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, type_name, hourly_rate FROM VehicleTypes")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                types.add(new VehicleType(
                        resultSet.getInt("id"),
                        resultSet.getString("type_name"),
                        resultSet.getDouble("hourly_rate")
                ));
            }
        }
        return types;
    }

    @Override
    public void addVehicleType(VehicleType vehicleType) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO VehicleTypes (type_name, hourly_rate) VALUES (?, ?)")) {

            preparedStatement.setString(1, vehicleType.getTypeName());
            preparedStatement.setDouble(2, vehicleType.getHourlyRate());
            preparedStatement.executeUpdate();
        }
    }
}