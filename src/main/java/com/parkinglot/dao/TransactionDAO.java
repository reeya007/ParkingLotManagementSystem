package com.parkinglot.dao;

import com.parkinglot.models.Transaction;
import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {
    List<Transaction> getAllTransactions() throws SQLException;
    void addTransaction(Transaction transaction) throws SQLException;
    void updateTransaction(Transaction transaction) throws SQLException;

}