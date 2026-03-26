
# =====================================================
# Stage 1: Build
# =====================================================
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copy dependency manifests first (better layer caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN ./mvnw package -DskipTests -B

# =====================================================
# Stage 2: Runtime
# =====================================================
FROM eclipse-temurin:25-jre-alpine AS runtime

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

USER spring

# Expose the default Spring Boot port
EXPOSE 8081

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
