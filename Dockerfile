FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY marketplace-common/pom.xml ./marketplace-common/pom.xml
COPY marketplace-common/src ./marketplace-common/src
RUN cd marketplace-common && mvn clean install -DskipTests

COPY marketplace-catalog/pom.xml ./marketplace-catalog/pom.xml
COPY marketplace-catalog/src ./marketplace-catalog/src
RUN cd marketplace-catalog && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/marketplace-catalog/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]