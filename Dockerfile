FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Копируем файлы Maven для кэширования зависимостей
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Скачиваем зависимости (отдельный слой для кэширования)
RUN mvn dependency:go-offline -B

# Копируем исходники и собираем
COPY src ./src
RUN mvn clean package -DskipTests -B

# Финальный образ
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Установка curl для health checks
RUN apk add --no-cache curl

# Копируем jar из builder
COPY --from=builder /app/target/*.jar app.jar

# Создаем непривилегированного пользователя
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

# Экспонируем порт
EXPOSE 8080

# Переменные окружения для JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/v1/actuator/health || exit 1

# Запуск приложения
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]