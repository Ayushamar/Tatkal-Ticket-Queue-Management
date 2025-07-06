# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven files
COPY backend/pom.xml ./backend/
COPY backend/src ./backend/src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Create a new stage for runtime
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=0 /app/backend/target/backend-0.0.1-SNAPSHOT.jar ./app.jar

# Create directory for PDF tokens
RUN mkdir -p pdf-tokens

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080

# Run the application
CMD ["java", "-jar", "app.jar"] 