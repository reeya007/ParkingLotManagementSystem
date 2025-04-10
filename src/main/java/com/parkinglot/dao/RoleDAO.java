package com.parkinglot.dao;

import com.parkinglot.models.UserRole;
import java.sql.SQLException;
import java.util.List;

public interface RoleDAO {
    List<UserRole> getAllRoles() throws SQLException;
}