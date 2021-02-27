# Build stage
FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY telegrindr .
RUN mvn clean package

#Package stage
FROM openjdk:11-jre-slim
COPY --from=build /app/target/telegrindr-1.0-SNAPSHOT.jar telegrindr.jar
EXPOSE 8080
ENTRYPOINT java $JAVA_OPTS -jar telegrindr.jar $TOKEN_ID $USERNAME $CREATOR_ID
