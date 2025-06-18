FROM openjdk:24 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# === 2️⃣ Runtime stage ===
FROM openjdk:24
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]