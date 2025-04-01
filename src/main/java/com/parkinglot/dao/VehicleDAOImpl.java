package com.parkinglot.dao;

import com.parkinglot.models.Vehicle;
import com.parkinglot.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOImpl implements VehicleDAO {

    @Override
    public void addVehicle(Vehicle vehicle) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Vehicles (user_id, vehicle_number, vehicle_type_id) VALUES (?, ?, ?)")) {

            preparedStatement.setInt(1, vehicle.getUserId());
            preparedStatement.setString(2, vehicle.getLicensePlate());
            preparedStatement.setInt(3, vehicle.getVehicleTypeId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Vehicle getVehicleByLicensePlate(String vehicleNumber) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, user_id, vehicle_number, vehicle_type_id FROM Vehicles WHERE vehicle_number = ?")) {

            preparedStatement.setString(1, vehicleNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Vehicle(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("vehicle_number"),
                        resultSet.getInt("vehicle_type_id")
                );
            }
            return null;
        }
    }

    @Override
    public List<Vehicle> getVehiclesByUser(int userId) throws SQLException{
        String sql = "SELECT * FROM Vehicles WHERE user_id = ?";
        List<Vehicle> vehicles = new ArrayList<>();
        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, userId);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    vehicles.add(new Vehicle(rs.getInt("id"), rs.getInt("user_id"), rs.getString("vehicle_number"), rs.getInt("vehicle_type_id")));
                }
                return vehicles;
            }
        }
    }
}