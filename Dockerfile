# Use OpenJDK as base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build output (JAR file) into the container
COPY target/carbookings-0.0.1-SNAPSHOT.jar carbookings.jar

# Expose port 8080 (or whichever port your Spring Boot app uses)
EXPOSE 8080

# Run the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "/app/carbookings.jar"]
