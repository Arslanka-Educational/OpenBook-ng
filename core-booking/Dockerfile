FROM gradle:7.4.2-jdk17 as builder

WORKDIR /core-booking

COPY gradlew /core-booking/
COPY gradle /core-booking/gradle
COPY build.gradle.kts /core-booking/
COPY settings.gradle.kts /core-booking/
COPY src /core-booking/src

RUN chmod +x gradlew

RUN ./gradlew build

FROM openjdk:17-jdk-slim

WORKDIR /core-booking

COPY build/libs/core-booking-1.0-SNAPSHOT.jar /openbook-ng/core-booking.jar

ENTRYPOINT ["java", "-jar", "/openbook-ng/core-booking.jar"]

EXPOSE 8080