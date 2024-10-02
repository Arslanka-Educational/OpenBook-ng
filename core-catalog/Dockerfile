FROM openjdk:17-jdk-slim

WORKDIR /core-catalog

COPY build/libs/core-catalog-1.0-SNAPSHOT.jar /openbook-ng/core-catalog.jar

ENTRYPOINT ["java", "-jar", "/openbook-ng/core-catalog.jar"]

EXPOSE 8081