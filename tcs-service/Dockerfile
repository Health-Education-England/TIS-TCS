FROM eclipse-temurin:11-alpine

EXPOSE 8093

COPY target/app.jar app.jar

CMD ["java", "-XX:MaxRAMPercentage=95.0", "-jar", "app.jar"]
