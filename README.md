# Tatkal Queue Management System

A full-stack application for managing railway tatkal ticket queues, built with Spring Boot and React.

## Project Structure

```
tatkal-queue-management/
├── backend/           # Spring Boot backend
└── frontend/         # React frontend
```

## Prerequisites

- Java 17 or higher
- Node.js and npm
- Maven
- MySQL 8.0 or higher
- Free ports:
  - 8080 (backend)
  - 5173 (frontend dev server)

## Quick Start

### 1. Database Setup

1. Install MySQL if not already installed
2. Login to MySQL:
   ```bash
   mysql -u root -p
   ```
3. Create the database:
   ```sql
   CREATE DATABASE tatkal_db;
   ```

### 2. Backend Setup

1. Navigate to backend directory:

   ```bash
   cd backend
   ```

2. Configure database:
   Create `src/main/resources/application.properties`:

   ```properties
   # MySQL Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/tatkal_db
   spring.datasource.username=root
   spring.datasource.password=root
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   # JPA
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

   # Server
   server.port=8080

   # API base path
   spring.mvc.servlet.path=/api

   # JWT Secret (Use environment variable in production)
   jwt.secret=your_secure_jwt_secret_key

   # PDF Output Directory
   pdf.output.dir=./pdf-tokens
   ```

3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### 3. Frontend Setup

1. Navigate to frontend directory:

   ```bash
   cd frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Run development server:
   ```bash
   npm run dev
   ```

## API Documentation

### Public Endpoints

#### OTP Management

- `POST /api/get-otp`

  - Generate OTP for Aadhaar verification
  - Body: `{ "aadhaar": "string" }`

- `POST /api/verify-otp`
  - Verify OTP
  - Body: `{ "aadhaar": "string", "otp": "string" }`

#### Registration

- `POST /api/register`
  - Register passenger and generate token
  - Returns: PDF token file
  - Body:
    ```json
    {
      "name": "string",
      "aadhaar": "string",
      "journeyDate": "string",
      "trainName": "string",
      "trainNumber": "string",
      "fromStation": "string",
      "toStation": "string",
      "coPassengers": [
        {
          "name": "string",
          "relation": "string",
          "aadhaar": "string"
        }
      ]
    }
    ```

### Admin Endpoints (Protected)

#### Authentication

- `POST /api/admin/login`
  - Admin login
  - Body: `{ "username": "string", "password": "string" }`
  - Returns: JWT token

#### Passenger Management

- `GET /api/admin/passengers`

  - List passengers with filters
  - Query params: `date`, `trainNo`, `status`, `page`, `size`

- `POST /api/admin/verify`

  - Verify passenger token
  - Body: `{ "tokenId": "string" }`

- `POST /api/admin/assign-counter`
  - Assign counter to passenger
  - Body: `{ "tokenId": "string", "counterNumber": "number" }`

#### Reports

- `GET /api/admin/logs/export`
  - Export passenger logs as CSV
  - Returns: CSV file

## Features

### User Features

- OTP-based Aadhaar verification
- Passenger registration with co-passenger support
- PDF token generation
- Token status tracking

### Admin Features

- Secure JWT authentication
- Passenger list with filters
- Token verification
- Counter assignment
- Export passenger logs

## Directory Structure

### Backend

```
backend/
├── src/main/java/com/tokenbackend/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST endpoints
│   ├── dto/             # Data Transfer Objects
│   ├── model/           # JPA entities
│   ├── repository/      # Data access layer
│   ├── service/         # Business logic
│   └── util/            # Utilities
└── src/main/resources/  # Configuration files
```

### Frontend

```
frontend/
├── src/
│   ├── components/      # Reusable components
│   ├── contexts/        # React contexts
│   ├── pages/          # Page components
│   └── utils/          # Utility functions
```

## Security Notes

1. Production Deployment:

   - Use environment variables for sensitive data
   - Change the JWT secret
   - Enable HTTPS
   - Configure CORS properly
   - Implement rate limiting

2. Auto-created Directories:
   - `pdf-tokens/` for storing generated tokens

## Error Handling

The API uses standard HTTP status codes:

- 200: Success
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 500: Internal Server Error

## Troubleshooting

1. Database Connection Issues:

   - Verify MySQL is running
   - Check credentials in application.properties
   - Ensure database exists

2. Port Conflicts:

   - Check if ports 8080 and 5173 are free
   - Change ports in configuration if needed

3. PDF Generation:
   - Ensure write permissions for pdf-tokens directory
   - Check disk space

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.
