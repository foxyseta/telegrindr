# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
COPY telegrindr/src src
COPY telegrindr/pom.xml pom.xml
RUN mvn -f pom.xml clean package

#Package stage
FROM openjdk:11-jre-slim
VOLUME /tmp
COPY --from=build target/telegrindr-1.0-SNAPSHOT.jar telegrindr.jar
ARG JAVA_OPTS
ARG TOKEN_ID
ARG USERNAME="TelegrindrBot"
ARG CREATOR_ID
ENV JAVA_OPTS=$JAVA_OPTS TOKEN_ID=$TOKEN_ID USERNAME=$USERNAME CREATOR_ID=$CREATOR_ID
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar telegrindr.jar $TOKEN_ID $USERNAME $CREATOR_ID