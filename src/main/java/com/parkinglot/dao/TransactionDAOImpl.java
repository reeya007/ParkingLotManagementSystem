package com.parkinglot.dao;

import com.parkinglot.models.Transaction;
import com.parkinglot.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, user_id, vehicle_id, parking_slot_id, entry_time, exit_time, duration_minutes, amount, payment_status FROM Transactions")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getInt("parking_slot_id"),
                        resultSet.getTimestamp("entry_time"),
                        resultSet.getTimestamp("exit_time"),
                        resultSet.getInt("duration_minutes"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("payment_status")
                ));
            }
        }
        return transactions;
    }
    @Override
    public void addTransaction(Transaction transaction) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Transactions (user_id, vehicle_id, parking_slot_id, entry_time, exit_time, duration_minutes, amount, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, transaction.getUserId());
            preparedStatement.setInt(2, transaction.getVehicleId());
            preparedStatement.setInt(3, transaction.getParkingSlotId());
            preparedStatement.setTimestamp(4, transaction.getEntryTime());
            preparedStatement.setTimestamp(5, transaction.getExitTime());
            preparedStatement.setInt(6, transaction.getDurationMinutes());
            preparedStatement.setDouble(7, transaction.getAmount());
            preparedStatement.setString(8, transaction.getPaymentStatus());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Transactions SET exit_time = ?, duration_minutes = ?, amount = ?, payment_status = ? WHERE id = ?")) {

            preparedStatement.setTimestamp(1, transaction.getExitTime());
            preparedStatement.setInt(2, transaction.getDurationMinutes());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getPaymentStatus());
            preparedStatement.setInt(5, transaction.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean hasPendingTransaction(int userId, int vehicleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Transactions WHERE user_id = ? AND vehicle_id = ? AND payment_status = 'Pending'";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, vehicleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking for pending transaction: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean hasPendingTransactionForVehicle(int vehicleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Transactions WHERE vehicle_id = ? AND payment_status = 'Pending'";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, vehicleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking for pending transaction for vehicle: " + e.getMessage(), e);
        }
        return false;
    }
}