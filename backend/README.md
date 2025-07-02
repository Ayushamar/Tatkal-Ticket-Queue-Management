# Railway Token Management System - Backend

A Spring Boot 3.2 backend for a railway token system with Aadhaar-based authentication.

## Features

- **Aadhaar-based Authentication**: Users authenticate using their Aadhaar number and OTP
- **Family-based Co-passengers**: Select co-passengers from the same family
- **Smart Counter Assignment**:
  - Counter 5 for female main passengers
  - Counters 1-4 (round-robin) for male main passengers
- **PDF Token Generation**: Generate downloadable PDF tokens
- **PostgreSQL Database**: Robust data storage with proper constraints

## Tech Stack

- **Spring Boot 3.2** with Java 17+
- **Spring Data JPA** for database operations
- **PostgreSQL** as the database
- **Caffeine** for in-memory OTP caching
- **OpenPDF** for PDF generation
- **Spring Security** (simplified, no authentication required)

## API Endpoints

### 1. OTP Management

- `POST /api/otp/send` - Send OTP to mobile number linked to Aadhaar
- `POST /api/otp/verify` - Verify OTP and return person details

### 2. Person & Family

- `GET /api/family/{aadhaarNo}` - Get family members for co-passenger selection
- `GET /api/stations` - Get list of available stations

### 3. Journey Management

- `POST /api/journey` - Create a new journey with token assignment
- `GET /api/journey/{tokenNo}/pdf` - Download PDF token

## Database Setup

### 1. Install PostgreSQL

Make sure PostgreSQL is installed and running on your system.

### 2. Create Database

```sql
CREATE DATABASE tatkal_db;
```

### 3. Run Schema Script

Execute the `src/main/resources/schema.sql` file in your PostgreSQL database.

### 4. Update Configuration

Update `application.properties` with your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tatkal_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Build and Run

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Sample Data

The schema includes sample data for testing:

- 4 families with 2 members each
- Aadhaar numbers: 123456789012 to 123456789019
- Mobile numbers: 9876543210 to 9876543217

## Testing the API

### 1. Send OTP

```bash
curl -X POST http://localhost:8080/api/otp/send \
  -H "Content-Type: application/json" \
  -d '{"aadhaarNo": "123456789012"}'
```

### 2. Verify OTP

```bash
curl -X POST http://localhost:8080/api/otp/verify \
  -H "Content-Type: application/json" \
  -d '{"aadhaarNo": "123456789012", "otp": "123456"}'
```

### 3. Get Family Members

```bash
curl http://localhost:8080/api/family/123456789012
```

### 4. Create Journey

```bash
curl -X POST http://localhost:8080/api/journey \
  -H "Content-Type: application/json" \
  -d '{
    "aadhaarNo": "123456789012",
    "station": "Patna Junction",
    "journeyDate": "2025-01-15",
    "trainNo": "12345",
    "coPassengers": ["123456789013"]
  }'
```

## Notes

- OTP is printed to console for development/testing
- Aadhaar numbers are masked in responses (XXXX-XXXX-1234)
- PDF tokens are saved in `./pdf-tokens/` directory
- CORS is configured for `http://localhost:5173` (Vite default)
