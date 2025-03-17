package com.parkinglot.services;

import com.parkinglot.dao.VehicleTypeDAO;
import com.parkinglot.dao.VehicleTypeDAOImpl;
import com.parkinglot.models.VehicleType;

import java.sql.SQLException;
import java.util.List;

public class VehicleTypeService {
    private final VehicleTypeDAO vehicleTypeDAO = new VehicleTypeDAOImpl();

    public List<VehicleType> getAllVehicleTypes() throws SQLException {
        return vehicleTypeDAO.getAllVehicleTypes();
    }

    public void addVehicleType(VehicleType vehicleType) throws SQLException {
        vehicleTypeDAO.addVehicleType(vehicleType);
    }
}