# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
COPY  . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-slim
COPY --from=build target/*.jar app.jar
EXPOSE 0
ENTRYPOINT ["java","-jar","/app.jar"]