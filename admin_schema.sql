-- Admin Schema for Queue Management System

-- Counter Table
CREATE TABLE IF NOT EXISTS counter (
    counter_id SERIAL PRIMARY KEY,
    counter_name VARCHAR(100) NOT NULL,
    counter_number INTEGER UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    assigned_staff_id VARCHAR(50),
    assigned_staff_name VARCHAR(100),
    current_queue_position INTEGER DEFAULT 0,
    total_served_today INTEGER DEFAULT 0,
    last_activity TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Staff Table
CREATE TABLE IF NOT EXISTS staff (
    staff_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    mobile_no VARCHAR(15),
    role VARCHAR(20) NOT NULL DEFAULT 'COUNTER_STAFF',
    assigned_counter_id INTEGER REFERENCES counter(counter_id),
    shift_start_time VARCHAR(5) DEFAULT '09:00',
    shift_end_time VARCHAR(5) DEFAULT '17:00',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    total_served_today INTEGER DEFAULT 0,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Token Rule Table
CREATE TABLE IF NOT EXISTS token_rule (
    rule_id SERIAL PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL,
    rule_type VARCHAR(20) NOT NULL,
    priority INTEGER NOT NULL DEFAULT 1,
    is_active BOOLEAN NOT NULL DEFAULT true,
    gender VARCHAR(10),
    train_number VARCHAR(10),
    station VARCHAR(100),
    assigned_counter INTEGER,
    counter_range_start INTEGER,
    counter_range_end INTEGER,
    start_time VARCHAR(5),
    end_time VARCHAR(5),
    max_tokens_per_day INTEGER,
    description TEXT,
    created_by VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_counter_status ON counter(status);
CREATE INDEX IF NOT EXISTS idx_counter_staff_id ON counter(assigned_staff_id);
CREATE INDEX IF NOT EXISTS idx_staff_role ON staff(role);
CREATE INDEX IF NOT EXISTS idx_staff_status ON staff(status);
CREATE INDEX IF NOT EXISTS idx_staff_counter ON staff(assigned_counter_id);
CREATE INDEX IF NOT EXISTS idx_token_rule_active ON token_rule(is_active);
CREATE INDEX IF NOT EXISTS idx_token_rule_type ON token_rule(rule_type);
CREATE INDEX IF NOT EXISTS idx_token_rule_priority ON token_rule(priority);

-- Sample data for testing
INSERT INTO counter (counter_name, counter_number, status) VALUES 
('Counter 1', 1, 'ACTIVE'),
('Counter 2', 2, 'ACTIVE'),
('Counter 3', 3, 'ACTIVE'),
('Counter 4', 4, 'ACTIVE'),
('Ladies Counter', 5, 'ACTIVE')
ON CONFLICT DO NOTHING;

INSERT INTO staff (staff_id, name, email, mobile_no, role) VALUES 
('ADMIN001', 'Admin User', 'admin@railway.com', '9876543210', 'ADMIN'),
('STAFF001', 'John Doe', 'john@railway.com', '9876543211', 'COUNTER_STAFF'),
('STAFF002', 'Jane Smith', 'jane@railway.com', '9876543212', 'COUNTER_STAFF'),
('STAFF003', 'Mike Johnson', 'mike@railway.com', '9876543213', 'SUPERVISOR'),
('STAFF004', 'Sarah Wilson', 'sarah@railway.com', '9876543214', 'COUNTER_STAFF')
ON CONFLICT DO NOTHING;

INSERT INTO token_rule (rule_name, rule_type, priority, description, created_by) VALUES 
('Female Counter Assignment', 'GENDER_BASED', 1, 'Assign female passengers to counter 5', 'ADMIN001'),
('Male Counter Assignment', 'GENDER_BASED', 2, 'Assign male passengers to counters 1-4', 'ADMIN001'),
('Express Train Priority', 'TRAIN_BASED', 3, 'Priority handling for express trains', 'ADMIN001'),
('Peak Hour Rule', 'TIME_BASED', 4, 'Special handling during peak hours', 'ADMIN001')
ON CONFLICT DO NOTHING; 