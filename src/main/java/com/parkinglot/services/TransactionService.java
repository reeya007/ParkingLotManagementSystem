package com.parkinglot.services;

import com.parkinglot.dao.TransactionDAO;
import com.parkinglot.dao.TransactionDAOImpl;
import com.parkinglot.models.Transaction;

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
}