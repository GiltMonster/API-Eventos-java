# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# Etapa final (imagem menor, só com o jar)
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*-runner.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
