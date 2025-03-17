package com.parkinglot.dao;

import com.parkinglot.models.VehicleType;
import java.sql.SQLException;
import java.util.List;

public interface VehicleTypeDAO {
    List<VehicleType> getAllVehicleTypes() throws SQLException;
    void addVehicleType(VehicleType vehicleType) throws SQLException;
}