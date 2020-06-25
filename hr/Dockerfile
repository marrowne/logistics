FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD

ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

COPY ./.m2 /root/.m2
COPY ./ ./

RUN mvn -P prod -Dspring.profiles.active=prod -Dmaven.test.skip=true clean package

FROM openjdk:8-jre-alpine3.9

COPY --from=MAVEN_BUILD target/hr-0.0.1-SNAPSHOT.jar /hr.jar

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "/hr.jar"]