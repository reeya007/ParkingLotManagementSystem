package com.parkinglot.services;

import com.parkinglot.dao.ParkingSlotDAO;
import com.parkinglot.dao.ParkingSlotDAOImpl;
import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.VehicleType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotService {
    private final ParkingSlotDAO parkingSlotDAO = new ParkingSlotDAOImpl();

    public List<ParkingSlot> getAllParkingSlots() throws SQLException {
        return parkingSlotDAO.getAllParkingSlots();
    }

    public void updateParkingSlot(ParkingSlot slot) throws SQLException {
        parkingSlotDAO.updateParkingSlot(slot);
    }

    public ParkingSlot getParkingSlotById(int slotId) throws SQLException {
        return parkingSlotDAO.getParkingSlotById(slotId);
    }

    public void addParkingSlotsInGrid(int floor, int rows, int columns, VehicleType vehicleType) throws SQLException {
        List<ParkingSlot> slots = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                String rowLabel = String.valueOf((char) ('A' + row));
                String label = "L" + floor + "-" + rowLabel + (col + 1);

                ParkingSlot slot = new ParkingSlot();
                slot.setFloor(floor);
                slot.setRowNum(rowLabel);
                slot.setColumnNum(col + 1);
                slot.setLabel(label);
                slot.setAvailable(true);
                slot.setAllowedVehicleTypeId(vehicleType.getId());

                slots.add(slot);
            }
        }
        parkingSlotDAO.addParkingSlots(slots);
    }
}