FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

COPY build/libs/classpilot-backend-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
