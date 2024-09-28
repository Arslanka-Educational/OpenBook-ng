FROM openjdk:17-jdk-slim

WORKDIR /core-booking

COPY build/libs/core-booking-1.0-SNAPSHOT.jar /openbook-ng/core-booking.jar

ENTRYPOINT ["java", "-jar", "/openbook-ng/core-booking.jar"]

EXPOSE 8080