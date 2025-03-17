package com.parkinglot.models;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int userId;
    private int vehicleId;
    private int parkingSlotId;
    private Timestamp entryTime;
    private Timestamp exitTime;
    private int durationMinutes;
    private double amount;
    private String paymentStatus;

    // Constructors
    public Transaction() {}

    public Transaction(int id, int userId, int vehicleId, int parkingSlotId, Timestamp entryTime, Timestamp exitTime, int durationMinutes, double amount, String paymentStatus) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.parkingSlotId = parkingSlotId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.durationMinutes = durationMinutes;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getParkingSlotId() { return parkingSlotId; }
    public void setParkingSlotId(int parkingSlotId) { this.parkingSlotId = parkingSlotId; }

    public Timestamp getEntryTime() { return entryTime; }
    public void setEntryTime(Timestamp entryTime) { this.entryTime = entryTime; }

    public Timestamp getExitTime() { return exitTime; }
    public void setExitTime(Timestamp exitTime) { this.exitTime = exitTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}