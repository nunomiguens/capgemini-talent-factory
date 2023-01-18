FROM openjdk:17-jdk-alpine
RUN mkdir -p /app
COPY target/consumer-0.1.0.jar /app
WORKDIR /app

CMD ["java", "-jar",  "consumer-0.1.0.jar"]
