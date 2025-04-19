FROM openjdk:21-jdk-slim
WORKDIR /app

# copy the fat‑JAR created by Maven
COPY target/mediaapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
