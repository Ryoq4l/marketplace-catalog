# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY settings.xml /root/.m2/settings.xml

COPY marketplace-common/pom.xml ./marketplace-common/pom.xml
COPY marketplace-common/src ./marketplace-common/src
RUN cd marketplace-common && mvn clean install -DskipTests -s /root/.m2/settings.xml

# Копируем именно CATALOG
COPY marketplace-catalog/pom.xml ./marketplace-catalog/pom.xml
COPY marketplace-catalog/src ./marketplace-catalog/src
RUN cd marketplace-catalog && mvn clean package -DskipTests -s /root/.m2/settings.xml

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/marketplace-catalog/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]