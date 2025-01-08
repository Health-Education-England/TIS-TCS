FROM openjdk:8-alpine

EXPOSE 8093

COPY target/app.jar app.jar

CMD ["java", "-XX:MaxRAMPercentage=95", "-jar", "app.jar"]
