CREATE TABLE UserRoles (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           role_name VARCHAR(50)
);

CREATE TABLE VehicleTypes (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              type_name VARCHAR(50),
                              hourly_rate DECIMAL(10, 2) NOT NULL DEFAULT 0.00;
);

CREATE TABLE ParkingSlots (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              floor INT,
                              row_num VARCHAR(10),
                              column_num INT,
                              label VARCHAR(20) UNIQUE,
                              is_available BOOLEAN,
                              allowed_vehicle_type_id INT,
                              FOREIGN KEY (allowed_vehicle_type_id) REFERENCES VehicleTypes(id)
);

CREATE TABLE Users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255),
                       address VARCHAR(255),
                       phone_number VARCHAR(20),
                       email VARCHAR(255) UNIQUE,
                       password_hash VARCHAR(255),
                       role_id INT,
                       assigned_slot_id INT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (role_id) REFERENCES UserRoles(id),
                       FOREIGN KEY (assigned_slot_id) REFERENCES ParkingSlots(id)
);

CREATE TABLE Vehicles (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          vehicle_number VARCHAR(20) UNIQUE,
                          vehicle_type_id INT,
                          user_id INT,
                          FOREIGN KEY (vehicle_type_id) REFERENCES VehicleTypes(id),
                          FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Transactions (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              user_id INT,
                              vehicle_id INT,
                              parking_slot_id INT,
                              entry_time TIMESTAMP,
                              exit_time TIMESTAMP,
                              duration_minutes INT,
                              amount DECIMAL(10, 2),
                              payment_status VARCHAR(20),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES Users(id),
                              FOREIGN KEY (vehicle_id) REFERENCES Vehicles(id),
                              FOREIGN KEY (parking_slot_id) REFERENCES ParkingSlots(id)
);