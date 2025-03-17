package com.parkinglot.models;

public class VehicleType {
    private int id;
    private String typeName;
    private double hourlyRate;

    // Constructors
    public VehicleType() {}

    public VehicleType(int id, String typeName, double hourlyRate) {
        this.id = id;
        this.typeName = typeName;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
}