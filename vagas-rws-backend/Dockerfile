# Use a imagem oficial do Maven como base
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2 ./mvnw clean install
# RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests
COPY . .

# Exponha a porta que a aplicação Spring Boot está escutando (por padrão, 8080)
EXPOSE 8080

# Comando para executar a aplicação Spring Boot quando o contêiner for iniciado
ENTRYPOINT ["java", "-jar", "target/vaga-rws-0.0.1-SNAPSHOT.jar"]