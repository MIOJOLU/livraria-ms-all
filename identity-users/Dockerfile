# Etapa 1 - build
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY . .

RUN ./gradlew build -x test

# Etapa 2 - runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]