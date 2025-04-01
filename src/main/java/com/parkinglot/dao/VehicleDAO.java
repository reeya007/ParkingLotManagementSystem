package com.parkinglot.dao;

import com.parkinglot.models.Vehicle;
import java.sql.SQLException;
import java.util.List;

public interface VehicleDAO {
    void addVehicle(Vehicle vehicle) throws SQLException;
    Vehicle getVehicleByLicensePlate(String licensePlate) throws SQLException;
    List<Vehicle> getVehiclesByUser(int userId) throws SQLException;
}
