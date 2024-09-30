FROM openjdk:17-jdk-slim

WORKDIR /gateway

COPY build/libs/gateway-1.0-SNAPSHOT.jar /openbook-ng/gateway.jar

ENTRYPOINT ["java", "-jar", "/openbook-ng/gateway.jar"]

EXPOSE 8080