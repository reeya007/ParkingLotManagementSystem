package com.parkinglot.services;

import com.parkinglot.dao.UserDAO;
import com.parkinglot.dao.UserDAOImpl;
import com.parkinglot.models.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAOImpl();

    public User registerUser(User user) throws SQLException {
        userDAO.addUser(user);
      return userDAO.getUserByEmail(user.getEmail());
    }

    public User getUserByEmail(String email) throws SQLException {
        return userDAO.getUserByEmail(email);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }
}