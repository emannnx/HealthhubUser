FROM ubuntu:latest AS BUILD
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:23
EXPOSE 8080
COPY --from=BUILD /build/libs/TestApplication-1.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]