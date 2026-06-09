# ==========================================
# STAGE 1: Build (Ambiente di Compilazione)
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia il pom.xml e scarica le dipendenze per sfruttare la cache di Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia il codice sorgente e compila il JAR escludendo i test per velocizzare
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Esecuzione (Immagine Minimale)
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia SOLO il file .jar generato dallo Stage 1, scartando Maven e il codice sorgente
COPY --from=build /app/target/*.jar app.jar

# Espone la porta standard
EXPOSE 8080

# Comando di avvio. Permetterà di passare il profilo Spring dinamicamente
ENTRYPOINT ["java", "-jar", "app.jar"]