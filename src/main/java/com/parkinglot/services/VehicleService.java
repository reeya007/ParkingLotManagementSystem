package com.parkinglot.services;

import com.parkinglot.dao.VehicleDAO;
import com.parkinglot.dao.VehicleDAOImpl;
import com.parkinglot.models.Vehicle;

import java.sql.SQLException;

public class VehicleService {
    private final VehicleDAO vehicleDAO = new VehicleDAOImpl();

    public void addVehicle(Vehicle vehicle) throws SQLException {
        vehicleDAO.addVehicle(vehicle);
    }

    public Vehicle getVehicleByLicensePlate(String licensePlate) throws SQLException {
        return vehicleDAO.getVehicleByLicensePlate(licensePlate);
    }
}