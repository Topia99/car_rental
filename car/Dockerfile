FROM eclipse-temurin:21-jre
ARG JAR=target/car-1.0-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR} app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]