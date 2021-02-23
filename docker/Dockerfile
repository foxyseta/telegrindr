FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY ../telegrindr.jar telegrindr.jar
EXPOSE 3000
ENV BOT_TOKEN=$BOT_TOKEN BOT_USERNAME=$BOT_USERNAME BOT_CREATOR_ID=$BOT_CREATOR_ID
ENTRYPOINT exec java $JAVA_OPTS -jar telegrindr.jar $BOT_TOKEN $BOT_USERNAME $BOT_CREATOR_ID
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar telegrindr.jar
