FROM eclipse-temurin:21-jre
ARG JAR=target/account-1.0-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR} app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]