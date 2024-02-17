FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ./target/Tasklist-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar", "/application.jar"]