package com.parkinglot.dao;

import com.parkinglot.models.ParkingSlot;
import java.sql.SQLException;
import java.util.List;

public interface ParkingSlotDAO {
    List<ParkingSlot> getAllParkingSlots() throws SQLException;
    void updateParkingSlot(ParkingSlot slot) throws SQLException;
    ParkingSlot getParkingSlotById(int slotId) throws SQLException;
    void addParkingSlots(List<ParkingSlot> slots) throws SQLException;
}