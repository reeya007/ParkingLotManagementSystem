package com.parkinglot.models;

public class User {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String passwordHash;
    private int roleId;
    private Integer assignedSlotId;

    public User() {}

    public User(int id, String name, String address, String phoneNumber, String email, String passwordHash, int roleId, Integer assignedSlotId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.assignedSlotId = assignedSlotId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getAssignedSlotId() {
        return assignedSlotId;
    }

    public void setAssignedSlotId(Integer assignedSlotId) {
        this.assignedSlotId = assignedSlotId;
    }
}