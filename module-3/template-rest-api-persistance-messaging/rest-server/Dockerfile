FROM openjdk:17-jdk-alpine
RUN mkdir -p /app
COPY target/rest-0.2.0.jar /app
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar",  "rest-0.2.0.jar"]
