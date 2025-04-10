package com.parkinglot.services;

import com.parkinglot.dao.TransactionDAO;
import com.parkinglot.dao.TransactionDAOImpl;
import com.parkinglot.models.Transaction;
import com.parkinglot.models.VehicleType;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO = new TransactionDAOImpl();

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDAO.getAllTransactions();
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        transactionDAO.addTransaction(transaction);
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        transactionDAO.updateTransaction(transaction);
    }

    /**
     * Method to calculate the parking fee
     *
     * @param durationMinutes
     * @param vehicleTypeId
     * @return parkingFee
     * @throws SQLException
     */
    public double calculateParkingFee(int durationMinutes, int vehicleTypeId) throws SQLException {
        VehicleTypeService vehicleTypeService = new VehicleTypeService();
        VehicleType vehicleType = vehicleTypeService.getAllVehicleTypes().stream()
                .filter(vt -> vt.getId() == vehicleTypeId)
                .findFirst()
                .orElse(null);

        if (vehicleType == null) {
            return 0.0;
        }

        if (durationMinutes <= 60) {
            return vehicleType.getHourlyRate();
        }

        return (durationMinutes / 60.0) * vehicleType.getHourlyRate();
    }

    public boolean hasPendingTransaction(int userId, int vehicleId) throws SQLException {
        return transactionDAO.hasPendingTransaction(userId, vehicleId);
    }

    public boolean hasPendingTransactionForVehicle(int vehicleId) throws SQLException {
        return transactionDAO.hasPendingTransactionForVehicle(vehicleId);
    }
}