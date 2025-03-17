package com.parkinglot.dao;

import com.parkinglot.models.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    void addUser(User user) throws SQLException;
    User getUserByEmail(String email) throws SQLException;
    List<User> getAllUsers() throws SQLException;
}