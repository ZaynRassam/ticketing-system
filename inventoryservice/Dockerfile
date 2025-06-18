# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set workdir inside the container
WORKDIR /app

# Copy pom.xml and download dependencies first (leverages Docker cache)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the source
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk-jammy

# Set workdir
WORKDIR /app

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (adjust if needed)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
