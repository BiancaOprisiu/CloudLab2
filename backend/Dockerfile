# Use OpenJDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build context (make sure to build the JAR first with Maven or Gradle)
COPY target/donationmanager-0.0.1-SNAPSHOT.jar /app/donationmanager-0.0.1-SNAPSHOT.jar

# Expose the port the backend will run on (default is 8080 for Spring Boot)
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "donationmanager-0.0.1-SNAPSHOT.jar"]
