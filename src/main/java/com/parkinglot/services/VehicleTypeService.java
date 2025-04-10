package com.parkinglot.services;

import com.parkinglot.dao.VehicleTypeDAO;
import com.parkinglot.dao.VehicleTypeDAOImpl;
import com.parkinglot.models.VehicleType;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for managing vehicle type related operations.
 */
public class VehicleTypeService {
    private final VehicleTypeDAO vehicleTypeDAO = new VehicleTypeDAOImpl();

    /**
     * Retrieves all vehicle types from the database.
     * @return A list of all VehicleType objects.
     *
     * @throws SQLException If a database error occurs.
     */
    public List<VehicleType> getAllVehicleTypes() throws SQLException {
        return vehicleTypeDAO.getAllVehicleTypes();
    }

    /**
     * Adds a new vehicle type to the database.
     * This method creates a VehicleType object and calls the DAO to persist it.
     *
     * @param typeName The name of the new vehicle type.
     * @param hourlyRate The hourly rate for parking this vehicle type.
     * @throws SQLException If a database error occurs during the insertion.
     */
    public void addVehicleType(String typeName, double hourlyRate) throws SQLException {
        VehicleType vehicleType = new VehicleType();
        vehicleType.setTypeName(typeName);
        vehicleType.setHourlyRate(hourlyRate);
        vehicleTypeDAO.addVehicleType(vehicleType);
    }
}