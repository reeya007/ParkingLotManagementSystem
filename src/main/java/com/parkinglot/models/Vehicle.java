package com.parkinglot.models;

public class Vehicle {
    private int id;
    private int userId;
    private String licensePlate;
    private int vehicleTypeId;

    // Constructors
    public Vehicle() {}

    public Vehicle(int id, int userId, String licensePlate, int vehicleTypeId) {
        this.id = id;
        this.userId = userId;
        this.licensePlate = licensePlate;
        this.vehicleTypeId = vehicleTypeId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int getVehicleTypeId() { return vehicleTypeId; }
    public void setVehicleTypeId(int vehicleTypeId) { this.vehicleTypeId = vehicleTypeId; }
}