FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml .

# Сначала собираем зависимости, чтобы использовать кэш
RUN ./mvnw dependency:go-offline

# Копируем и собираем только исходный код
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build app/target/*.jar app.jar

EXPOSE 8080

# Используем более эффективные параметры JVM
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xmx512m", "-jar", "app.jar"]