FROM adoptopenjdk:11-jre-hotspot
MAINTAINER Amanuel
WORKDIR /opt/application
ARG JAR_FILE=target/ems-1.0.0.jar
COPY ${JAR_FILE} application.jar

EXPOSE 8080/tcp

ENTRYPOINT ["java", "-jar", "application.jar"]