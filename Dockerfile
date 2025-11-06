# ===============================
# Stage 1 — Build
# ===============================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamos pom y descargamos dependencias primero (mejor cacheo)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código y construimos el .jar
COPY src ./src
RUN mvn clean package -DskipTests

# ===============================
# Stage 2 — Run
# ===============================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos solo el .jar desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
