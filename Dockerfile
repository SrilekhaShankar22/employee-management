# Use Java 21 runtime
FROM eclipse-temurin:21-jdk

# Create app directory
WORKDIR /app

# Copy jar file
COPY target/employee-management-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Start application
ENTRYPOINT ["java","-jar","app.jar"]