package com.parkinglot.services;

import com.parkinglot.dao.UserDAO;
import com.parkinglot.dao.UserDAOImpl;
import com.parkinglot.dao.VehicleDAO;
import com.parkinglot.dao.VehicleDAOImpl;
import com.parkinglot.models.User;
import com.parkinglot.models.Vehicle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

/**
 * Service class for managing user related operations, including registration and retrieval.
 */
public class UserService {
    private final UserDAO userDAO = new UserDAOImpl();
    private final VehicleDAO vehicleDAO = new VehicleDAOImpl();

    /**
     * Registers a new user with the role of Customer and their initial vehicle.
     * This method creates a User object, hashes the password, sets the role ID to 2 (Customer),
     * persists the user to the database, and then registers the initial vehicle for the user.
     * @param name The name of the customer.
     * @param address The address of the customer.
     * @param phoneNumber The phone number of the customer.
     * @param email The unique email address of the customer.
     * @param password The plain text password for the customer (will be hashed).
     * @param vehicleNumber The license plate number of the customer's initial vehicle.
     * @param vehicleTypeId The ID of the vehicle type of the customer's initial vehicle.
     * @throws SQLException If a database error occurs during registration.
     */
    public void registerCustomer(String name, String address, String phoneNumber, String email, String password, String vehicleNumber, int vehicleTypeId) throws SQLException {
        User user = new User();
        user.setName(name);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPasswordHash(hashPassword(password));
        user.setRoleId(2); // 2 is the role_id for Customer
        userDAO.addUser(user);

        User registeredUser =  userDAO.getUserByEmail(email);

        if (registeredUser != null) {
            Vehicle vehicle = new Vehicle();
            vehicle.setUserId(registeredUser.getId());
            vehicle.setLicensePlate(vehicleNumber);
            vehicle.setVehicleTypeId(vehicleTypeId);
            vehicleDAO.addVehicle(vehicle);
        }
    }

    /**
     * Registers a new user with the role of Admin.
     * This method creates a User object, hashes the password, sets the role ID to 1 (Admin),
     * persists the user to the database, and then retrieves the registered user by email.
     * @param name The name of the admin user.
     * @param address The address of the admin user.
     * @param phoneNumber The phone number of the admin user.
     * @param email The unique email address of the admin user.
     * @param password The plain text password for the admin user (will be hashed).
     * @return The registered User object, or null if registration fails.
     * @throws SQLException If a database error occurs during registration.
     */
    public User registerAdmin(String name, String address, String phoneNumber, String email, String password) throws SQLException {
        User user = new User();
        user.setName(name);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPasswordHash(hashPassword(password));
        user.setRoleId(1); // 1 is the role_id for Admin
        userDAO.addUser(user);
        return getUserByEmail(user.getEmail());
    }

    /**
     * Retrieves a user from the database based on their email address.
     * @param email The email address of the user to retrieve.
     * @return The User object with the given email, or null if not found.
     * @throws SQLException If a database error occurs.
     */
    public User getUserByEmail(String email) throws SQLException {
        return userDAO.getUserByEmail(email);
    }

    /**
     * Retrieves all users from the database.
     * @return A list of all User objects.
     * @throws SQLException If a database error occurs.
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    /**
     * Updates the details of an existing user in the database.
     * @param user The User object containing the updated information.
     * @throws SQLException If a database error occurs.
     */
    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    /**
     * Retrieves a user from the database based on their ID.
     * @param userId The ID of the user to retrieve.
     * @return The User object with the given ID, or null if not found.
     * @throws SQLException If a database error occurs.
     */
    public User getUserById(int userId) throws SQLException {
        return userDAO.getUserById(userId);
    }

    /**
     * Hashes the given password using SHA-256 algorithm.
     * @param password The plain text password to hash.
     * @return The hexadecimal representation of the SHA-256 hash, or null if an error occurs.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a byte array to its hexadecimal string representation.
     * @param hash The byte array to convert.
     * @return The hexadecimal string representation of the byte array.
     */
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}