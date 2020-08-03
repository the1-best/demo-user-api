FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/demo-user-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
ENV JAVA_OPTS=""
ENV TZ Asia/Bangkok
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]