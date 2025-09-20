# ---------------------------
# Stage 1: Build
# ---------------------------
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first for caching dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# ---------------------------
# Stage 2: Runtime
# ---------------------------
FROM eclipse-temurin:17-jdk-focal
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/inventory-management-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
