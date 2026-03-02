# Docker Build Stage
FROM maven:3.9.11-eclipse-temurin-25 AS build

# Build Stage
WORKDIR /opt/app

COPY ./ /opt/app
RUN mvn -pl infrastructure -am clean package -DskipTests

# Docker Build Stage
FROM eclipse-temurin:25-jre-jammy

COPY --from=build /opt/app/infrastructure/target/*.jar app.jar

ENV PORT 8081
EXPOSE $PORT

ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","app.jar"]
