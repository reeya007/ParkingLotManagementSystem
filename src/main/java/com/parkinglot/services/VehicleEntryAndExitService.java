package com.parkinglot.services;

import com.parkinglot.models.ParkingSlot;
import com.parkinglot.models.Transaction;
import com.parkinglot.models.User;
import com.parkinglot.models.Vehicle;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.sql.SQLException;

/**
 * Service class responsible for handling parking transaction related operations,
 * including vehicle entry and exit. It orchestrates interactions with other services.
 */
public class VehicleEntryAndExitService {

    private final VehicleService vehicleService = new VehicleService();
    private final ParkingSlotService parkingSlotService = new ParkingSlotService();
    private final TransactionService transactionService = new TransactionService();
    private final UserService userService = new UserService();
    private final VehicleTypeService vehicleTypeService = new VehicleTypeService();

    /**
     * Processes the entry of a vehicle into the parking lot.
     * This method checks for existing pending entries, finds an available slot,
     * creates a new transaction, updates the parking slot availability, and updates the user's assigned slot.
     * @param licensePlate The license plate number of the entering vehicle.
     * @throws SQLException If a database error occurs during any of the operations.
     * @throws IllegalArgumentException If the vehicle is not found or already has a pending entry,
     * or if no suitable parking slot is available.
     */
    public void processVehicleEntry(String licensePlate) throws SQLException, IllegalArgumentException {
        Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found.");
        }

        // Check if there's already a pending transaction for this vehicle
        boolean hasPendingEntry = transactionService.hasPendingTransactionForVehicle(vehicle.getId());
        if (hasPendingEntry) {
            throw new IllegalArgumentException("This vehicle already has a pending entry.");
        }

        List<ParkingSlot> availableSlots = parkingSlotService.getAllParkingSlots().stream()
                .filter(ParkingSlot::isIsAvailable)
                .filter(slot -> slot.getAllowedVehicleTypeId() == vehicle.getVehicleTypeId())
                .toList();

        if (availableSlots.isEmpty()) {
            throw new IllegalArgumentException("No available parking slots for this vehicle type.");
        }

        ParkingSlot assignedSlot = availableSlots.getFirst();
        assignedSlot.setAvailable(false);
        parkingSlotService.updateParkingSlot(assignedSlot);

        Transaction transaction = new Transaction();
        transaction.setUserId(vehicle.getUserId());
        transaction.setVehicleId(vehicle.getId());
        transaction.setParkingSlotId(assignedSlot.getId());
        transaction.setEntryTime(new Timestamp(System.currentTimeMillis()));
        transaction.setPaymentStatus("Pending");

        transactionService.addTransaction(transaction);

        // Update the user's assignedSlotId
        User user = userService.getUserById(vehicle.getUserId());
        if (user != null) {
            user.setAssignedSlotId(assignedSlot.getId());
            userService.updateUser(user);
        }
    }

    /**
     * Processes the exit of a vehicle from the parking lot.
     * This method retrieves the active transaction for the vehicle, updates the exit time,
     * calculates the parking fee, updates the transaction details, and makes the parking slot available again.
     * @param licensePlate The license plate number of the exiting vehicle.
     * @return The total parking fee for the duration.
     * @throws SQLException If a database error occurs during any of the operations.
     * @throws IllegalArgumentException If the vehicle is not found or no active transaction exists for it.
     */
    public double processVehicleExit(String licensePlate) throws SQLException, IllegalArgumentException {
        Vehicle vehicle = vehicleService.getVehicleByLicensePlate(licensePlate);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found.");
        }

        List<Transaction> activeTransactions = transactionService.getAllTransactions().stream()
                .filter(t -> t.getVehicleId() == vehicle.getId())
                .filter(t -> t.getExitTime() == null)
                .toList();

        if (activeTransactions.isEmpty()) {
            throw new IllegalArgumentException("No active transactions found for this vehicle.");
        }

        Transaction transaction = activeTransactions.getFirst();
        ParkingSlot assignedSlot = parkingSlotService.getParkingSlotById(transaction.getParkingSlotId());
        assignedSlot.setAvailable(true);
        parkingSlotService.updateParkingSlot(assignedSlot);

        Timestamp exitTime = new Timestamp(System.currentTimeMillis());
        transaction.setExitTime(exitTime);

        LocalDateTime entryDateTime = transaction.getEntryTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime exitDateTime = exitTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Duration duration = Duration.between(entryDateTime, exitDateTime);
        int durationMinutes = (int) duration.toMinutes();
        transaction.setDurationMinutes(durationMinutes);

        double amount = transactionService.calculateParkingFee(durationMinutes, vehicle.getVehicleTypeId());
        transaction.setAmount(amount);
        transaction.setPaymentStatus("Completed");

        transactionService.updateTransaction(transaction);

        // Clear the user's assigned slot ID upon exit
        User user = userService.getUserById(transaction.getUserId());
        if (user != null && user.getAssignedSlotId() != null && user.getAssignedSlotId().equals(assignedSlot.getId())) {
            user.setAssignedSlotId(null);
            userService.updateUser(user);
        }

        return amount;
    }
}