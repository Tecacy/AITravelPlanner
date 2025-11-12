# Multi-stage build: frontend (Vite) + backend (Spring Boot)

FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json* ./
RUN npm install
COPY frontend/ .
RUN npm run build

FROM maven:3.9.9-eclipse-temurin-17 AS backend-builder
WORKDIR /app/backend
COPY backend/pom.xml ./
COPY backend/src ./src
RUN mvn -DskipTests package

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
ENV SPRING_WEB_RESOURCES_STATIC_LOCATIONS=file:/app/static/
ENV SERVER_PORT=8080
COPY --from=backend-builder /app/backend/target/*.jar /app/app.jar
COPY --from=frontend-builder /app/frontend/dist /app/static
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]