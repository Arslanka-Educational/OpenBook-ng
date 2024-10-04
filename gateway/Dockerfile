FROM gradle:7.4.2-jdk17 as builder

WORKDIR /gateway

COPY gradlew /gateway/
COPY gradle /gateway/gradle
COPY build.gradle.kts /gateway/
COPY settings.gradle.kts /gateway/
COPY src /gateway/src

RUN chmod +x gradlew

RUN ./gradlew build

FROM openjdk:17-jdk-slim

WORKDIR /gateway

COPY build/libs/gateway-1.0-SNAPSHOT.jar /openbook-ng/gateway.jar

ENTRYPOINT ["java", "-jar", "/openbook-ng/gateway.jar"]

EXPOSE 8080