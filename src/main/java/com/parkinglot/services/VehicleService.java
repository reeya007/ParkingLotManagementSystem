package com.parkinglot.services;

import com.parkinglot.dao.VehicleDAO;
import com.parkinglot.dao.VehicleDAOImpl;
import com.parkinglot.models.Vehicle;

import java.sql.SQLException;
import java.util.List;

public class VehicleService {
    private final VehicleDAO vehicleDAO = new VehicleDAOImpl();

    public void addVehicle(Vehicle vehicle) throws SQLException {
        vehicleDAO.addVehicle(vehicle);
    }

    public Vehicle getVehicleByLicensePlate(String licensePlate) throws SQLException {
        return vehicleDAO.getVehicleByLicensePlate(licensePlate);
    }

    public List<Vehicle> getVehiclesByUser(int userId) throws SQLException{
        return vehicleDAO.getVehiclesByUser(userId);
    }
}