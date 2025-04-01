package com.parkinglot.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ParkingSlot {
    private int id;
    private int floor;
    private String rowNum;
    private int columnNum;
    private String label;
    private BooleanProperty available; // Using BooleanProperty
    private int allowedVehicleTypeId;

    // Constructors
    public ParkingSlot() {
        this.available = new SimpleBooleanProperty();
    }

    public ParkingSlot(int id, int floor, String rowNum, int columnNum, String label, boolean available, int allowedVehicleTypeId) {
        this.id = id;
        this.floor = floor;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.label = label;
        this.available = new SimpleBooleanProperty(available);
        this.allowedVehicleTypeId = allowedVehicleTypeId;
    }

    // Getters and Setters (using BooleanProperty)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BooleanProperty isAvailableProperty() {
        return available;
    }

    public boolean isIsAvailable() { // Getter for the boolean value
        return available.get();
    }

    public void setAvailable(boolean available) {
        this.available.set(available);
    }

    public int getAllowedVehicleTypeId() {
        return allowedVehicleTypeId;
    }

    public void setAllowedVehicleTypeId(int allowedVehicleTypeId) {
        this.allowedVehicleTypeId = allowedVehicleTypeId;
    }
}
