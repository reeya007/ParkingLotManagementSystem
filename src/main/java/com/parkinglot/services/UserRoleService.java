package com.parkinglot.services;

import com.parkinglot.dao.RoleDAO;
import com.parkinglot.dao.RoleDAOImpl;
import com.parkinglot.models.UserRole;

import java.sql.SQLException;
import java.util.List;

public class UserRoleService {
    private final RoleDAO roleDao = new RoleDAOImpl();

    public List<UserRole> getAllRoles() throws SQLException {
        return roleDao.getAllRoles();
    }
}
