-- Customer Table
CREATE TABLE IF NOT EXISTS Customer (
    id INT PRIMARY KEY,
    customer_name VARCHAR(255),
    customer_email VARCHAR(255)
    );

-- Address Table
CREATE TABLE IF NOT EXISTS Address (
    id INT PRIMARY KEY,
    address_street VARCHAR(255),
    address_city VARCHAR(255),
    address_zip INT
    );

-- (Joined Table for Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS Customer_Address (
    customer_id INT,
    address_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customer (id),
    FOREIGN KEY (address_id) REFERENCES Address (id)
    );

-- Machine Table
CREATE TABLE IF NOT EXISTS Machine (
    id INT PRIMARY KEY,
    machine_name VARCHAR(255),
    machine_type VARCHAR(255)
    );

-- Order Table
CREATE TABLE IF NOT EXISTS "Order" (
    id INT PRIMARY KEY,
    order_date TIMESTAMP,
    customer_id INT,
    address_id INT,
    machine_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customer (id),
    FOREIGN KEY (address_id) REFERENCES Address (id),
    FOREIGN KEY (machine_id) REFERENCES Machine (id)
    );

-- Subassembly Table
CREATE TABLE IF NOT EXISTS Subassembly (
    id INT PRIMARY KEY,
    subassembly_name VARCHAR(255),
    machine_id INT,
    FOREIGN KEY (machine_id) REFERENCES Machine (id)
    );

-- Part Table
CREATE TABLE IF NOT EXISTS Part (
    id INT PRIMARY KEY,
    part_name VARCHAR(255),
    subassembly_id INT,
    FOREIGN KEY (subassembly_id) REFERENCES Subassembly (id)
    );
