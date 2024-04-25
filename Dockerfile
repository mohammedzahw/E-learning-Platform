# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
COPY  . .
RUN mvn clean install
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-slim
COPY --from=build target/E-learning-Platform-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
