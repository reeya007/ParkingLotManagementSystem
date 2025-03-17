package com.parkinglot.models;

public class ParkingSlot {
    private int id;
    private int floor;
    private String rowNum;
    private int columnNum;
    private String label;
    private boolean isAvailable;
    private int allowedVehicleTypeId;

    // Constructors
    public ParkingSlot() {}

    public ParkingSlot(int id, int floor, String rowNum, int columnNum, String label, boolean isAvailable, int allowedVehicleTypeId) {
        this.id = id;
        this.floor = floor;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.label = label;
        this.isAvailable = isAvailable;
        this.allowedVehicleTypeId = allowedVehicleTypeId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public String getRowNum() { return rowNum; }
    public void setRowNum(String rowNum) { this.rowNum = rowNum; }

    public int getColumnNum() { return columnNum; }
    public void setColumnNum(int columnNum) { this.columnNum = columnNum; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getAllowedVehicleTypeId() { return allowedVehicleTypeId; }
    public void setAllowedVehicleTypeId(int allowedVehicleTypeId) { this.allowedVehicleTypeId = allowedVehicleTypeId; }
}