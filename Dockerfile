FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/feedback-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]

