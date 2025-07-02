-- PostgreSQL Schema for Railway Token System

-- 1. Family Table
CREATE TABLE IF NOT EXISTS family (
    family_id SERIAL PRIMARY KEY,
    family_name VARCHAR(100)
);

-- 2. Person Table
CREATE TABLE IF NOT EXISTS person (
    aadhaar_no CHAR(12) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age > 0),
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female')),
    mobile_no CHAR(10) NOT NULL,
    address TEXT,
    family_id INT REFERENCES family(family_id),
    CONSTRAINT chk_aadhaar_format CHECK (aadhaar_no ~ '^[0-9]{12}$'),
    CONSTRAINT chk_mobile_format CHECK (mobile_no ~ '^[0-9]{10}$')
);

CREATE INDEX IF NOT EXISTS idx_person_family_id ON person(family_id);

-- 3. Journey Table
CREATE TABLE IF NOT EXISTS journey (
    journey_id SERIAL PRIMARY KEY,
    main_aadhaar CHAR(12) REFERENCES person(aadhaar_no),
    station VARCHAR(100) NOT NULL,
    journey_date DATE NOT NULL,
    train_no VARCHAR(10),
    token_no SERIAL UNIQUE,
    counter_no INT,
    counter_position INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_journey_token_no ON journey(token_no);
CREATE INDEX IF NOT EXISTS idx_journey_counter_no ON journey(counter_no);

-- 4. Co-Passenger Table
CREATE TABLE IF NOT EXISTS co_passenger (
    id SERIAL PRIMARY KEY,
    journey_id INT REFERENCES journey(journey_id) ON DELETE CASCADE,
    aadhaar_no CHAR(12) REFERENCES person(aadhaar_no),
    UNIQUE (journey_id, aadhaar_no)
);

-- Sample data for testing
INSERT INTO family (family_name) VALUES 
('Sharma Family'),
('Kumar Family'),
('Singh Family'),
('Patel Family')
ON CONFLICT DO NOTHING;

INSERT INTO person (aadhaar_no, name, age, gender, mobile_no, address, family_id) VALUES 
('123456789012', 'Rahul Sharma', 25, 'Male', '9876543210', 'Patna, Bihar', 1),
('123456789013', 'Priya Sharma', 22, 'Female', '9876543211', 'Patna, Bihar', 1),
('123456789014', 'Amit Kumar', 30, 'Male', '9876543212', 'Delhi, India', 2),
('123456789015', 'Neha Kumar', 28, 'Female', '9876543213', 'Delhi, India', 2),
('123456789016', 'Raj Singh', 35, 'Male', '9876543214', 'Mumbai, Maharashtra', 3),
('123456789017', 'Kavita Singh', 32, 'Female', '9876543215', 'Mumbai, Maharashtra', 3),
('123456789018', 'Vikram Patel', 40, 'Male', '9876543216', 'Ahmedabad, Gujarat', 4),
('123456789019', 'Sunita Patel', 38, 'Female', '9876543217', 'Ahmedabad, Gujarat', 4)
ON CONFLICT DO NOTHING; 