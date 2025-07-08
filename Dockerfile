# Fase de construcción
FROM maven:3.8.6-openjdk-18 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Construye el proyecto ignorando los tests
RUN mvn package -DskipTests

# Fase de ejecución
FROM openjdk:8-jdk-alpine
COPY --from=builder /app/target/*.jar app.jar