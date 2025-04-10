package com.parkinglot.services;

import com.parkinglot.dao.ParkingSlotDAO;
import com.parkinglot.dao.ParkingSlotDAOImpl;
import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.VehicleType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing parking slot related operations.
 */
public class ParkingSlotService {
    private final ParkingSlotDAO parkingSlotDAO = new ParkingSlotDAOImpl();

    /**
     * Retrieves all parking slots from the database.
     *
     * @return A list of all ParkingSlot objects.
     * @throws SQLException If a database error occurs.
     */
    public List<ParkingSlot> getAllParkingSlots() throws SQLException {
        return parkingSlotDAO.getAllParkingSlots();
    }

    /**
     * Updates the details of an existing parking slot in the database.
     *
     * @param slot The ParkingSlot object containing the updated information.
     * @throws SQLException If a database error occurs.
     */
    public void updateParkingSlot(ParkingSlot slot) throws SQLException {
        parkingSlotDAO.updateParkingSlot(slot);
    }

    /**
     * Retrieves a specific parking slot from the database based on its ID.
     *
     * @param slotId The ID of the parking slot to retrieve.
     * @return The ParkingSlot object with the given ID, or null if not found.
     * @throws SQLException If a database error occurs.
     */
    public ParkingSlot getParkingSlotById(int slotId) throws SQLException {
        return parkingSlotDAO.getParkingSlotById(slotId);
    }

    /**
     * Adds multiple parking slots to the database based on the provided grid parameters and vehicle type.
     * This method generates the parking slot labels and sets their initial availability.
     *
     * @param floor The floor number where the parking slots are located.
     * @param rows The number of rows in the parking slot grid.
     * @param columns The number of columns in the parking slot grid.
     * @param vehicleType The VehicleType for which these parking slots are designated.
     * @throws SQLException If a database error occurs during the insertion of slots.
     */
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