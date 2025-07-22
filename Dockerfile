FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY build/libs/monapp-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "monapp-0.0.1-SNAPSHOT.jar"]
