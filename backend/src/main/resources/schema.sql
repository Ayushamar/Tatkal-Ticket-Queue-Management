-- Updated schema for Tatkal Queue Management System

-- Person table
CREATE TABLE IF NOT EXISTS person (
    aadhaar_no VARCHAR(12) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    dob DATE,
    age INTEGER,
    mobile VARCHAR(15),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Journey table with token_issue_date
CREATE TABLE IF NOT EXISTS journey (
    journey_id SERIAL PRIMARY KEY,
    main_aadhaar VARCHAR(12) NOT NULL,
    station VARCHAR(100) NOT NULL,
    journey_date DATE NOT NULL,
    train_no VARCHAR(10),
    token_no INTEGER,
    token_issue_date DATE NOT NULL,
    counter_no INTEGER,
    counter_position INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (main_aadhaar) REFERENCES person(aadhaar_no)
);

-- Co-passenger table
CREATE TABLE IF NOT EXISTS co_passenger (
    id SERIAL PRIMARY KEY,
    journey_id INTEGER NOT NULL,
    aadhaar_no VARCHAR(12) NOT NULL,
    FOREIGN KEY (journey_id) REFERENCES journey(journey_id) ON DELETE CASCADE,
    FOREIGN KEY (aadhaar_no) REFERENCES person(aadhaar_no)
);

-- Train table
CREATE TABLE IF NOT EXISTS train (
    train_no VARCHAR(10) PRIMARY KEY,
    train_name VARCHAR(200) NOT NULL,
    source_station VARCHAR(100),
    destination_station VARCHAR(100),
    departure_time TIME,
    arrival_time TIME
);

-- Train route table
CREATE TABLE IF NOT EXISTS train_route (
    id SERIAL PRIMARY KEY,
    train_no VARCHAR(10) NOT NULL,
    station_code VARCHAR(10) NOT NULL,
    station_name VARCHAR(100) NOT NULL,
    arrival_time TIME,
    departure_time TIME,
    sequence_no INTEGER NOT NULL,
    FOREIGN KEY (train_no) REFERENCES train(train_no)
);

-- Family table
CREATE TABLE IF NOT EXISTS family (
    id SERIAL PRIMARY KEY,
    main_aadhaar VARCHAR(12) NOT NULL,
    member_aadhaar VARCHAR(12) NOT NULL,
    relation VARCHAR(50) NOT NULL,
    FOREIGN KEY (main_aadhaar) REFERENCES person(aadhaar_no),
    FOREIGN KEY (member_aadhaar) REFERENCES person(aadhaar_no)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_journey_token_issue_date ON journey(token_issue_date);
CREATE INDEX IF NOT EXISTS idx_journey_counter_date ON journey(counter_no, token_issue_date);
CREATE INDEX IF NOT EXISTS idx_journey_token_no ON journey(token_no);
CREATE INDEX IF NOT EXISTS idx_person_aadhaar ON person(aadhaar_no);
CREATE INDEX IF NOT EXISTS idx_train_route_train_no ON train_route(train_no); 