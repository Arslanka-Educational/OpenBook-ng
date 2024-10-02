FROM gradle:7.4.2-jdk17 as builder

WORKDIR /core-catalog

COPY gradlew /core-catalog/
COPY gradle /core-catalog/gradle
COPY build.gradle.kts /core-catalog/
COPY settings.gradle.kts /core-catalog/
COPY src /core-catalog/src

RUN chmod +x gradlew

RUN ./gradlew build

FROM openjdk:17-jdk-slim

WORKDIR /core-catalog

COPY build/libs/core-catalog-1.0-SNAPSHOT.jar /openbook-ng/core-catalog.jar

ENTRYPOINT ["java", "-jar", "/openbook-ng/core-catalog.jar"]

EXPOSE 8081