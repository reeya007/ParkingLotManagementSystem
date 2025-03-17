package com.parkinglot.dao;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotDAOImpl implements ParkingSlotDAO {

    @Override
    public List<ParkingSlot> getAllParkingSlots() throws SQLException {
        List<ParkingSlot> slots = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, floor, row_num, column_num, label, is_available, allowed_vehicle_type_id FROM ParkingSlots")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                slots.add(new ParkingSlot(
                        resultSet.getInt("id"),
                        resultSet.getInt("floor"),
                        resultSet.getString("row_num"),
                        resultSet.getInt("column_num"),
                        resultSet.getString("label"),
                        resultSet.getBoolean("is_available"),
                        resultSet.getInt("allowed_vehicle_type_id")
                ));
            }
        }
        return slots;
    }

    @Override
    public void updateParkingSlot(ParkingSlot slot) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE ParkingSlots SET is_available = ? WHERE id = ?")) {

            preparedStatement.setBoolean(1, slot.isAvailable());
            preparedStatement.setInt(2, slot.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public ParkingSlot getParkingSlotById(int slotId) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, floor, row_num, column_num, label, is_available, allowed_vehicle_type_id FROM ParkingSlots WHERE id = ?")) {

            preparedStatement.setInt(1, slotId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new ParkingSlot(
                        resultSet.getInt("id"),
                        resultSet.getInt("floor"),
                        resultSet.getString("row_num"),
                        resultSet.getInt("column_num"),
                        resultSet.getString("label"),
                        resultSet.getBoolean("is_available"),
                        resultSet.getInt("allowed_vehicle_type_id")
                );
            }
        }
        return null;
    }

    @Override
    public void addParkingSlots(List<ParkingSlot> slots) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO ParkingSlots (floor, row_num, column_num, label, is_available, allowed_vehicle_type_id) VALUES (?, ?, ?, ?, ?, ?)")) {

            for (ParkingSlot slot : slots) {
                preparedStatement.setInt(1, slot.getFloor());
                preparedStatement.setString(2, slot.getRowNum());
                preparedStatement.setInt(3, slot.getColumnNum());
                preparedStatement.setString(4, slot.getLabel());
                preparedStatement.setBoolean(5, slot.isAvailable());
                preparedStatement.setInt(6, slot.getAllowedVehicleTypeId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}