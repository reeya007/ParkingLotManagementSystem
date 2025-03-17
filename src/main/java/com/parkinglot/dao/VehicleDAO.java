package com.parkinglot.dao;

import com.parkinglot.models.Vehicle;
import java.sql.SQLException;

public interface VehicleDAO {
    void addVehicle(Vehicle vehicle) throws SQLException;
    Vehicle getVehicleByLicensePlate(String licensePlate) throws SQLException;
}
