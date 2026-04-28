# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the entire backend directory
COPY backend/ ./backend/

# Build the application
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Create directory for PDF tokens
RUN mkdir -p pdf-tokens

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080

# Run the application
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"] 