# --------- Build stage (JDK 25) ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copiamos wrapper y pom primero para cachear dependencias
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw -DskipTests dependency:go-offline

# Copiamos el código
COPY src ./src

# Compilamos
RUN ./mvnw -DskipTests clean package

# --------- Run stage (JRE 25) ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
