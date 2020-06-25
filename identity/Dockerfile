FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD

COPY ./ ./

RUN mvn -P prod -Dspring.profiles.active=prod -Dmaven.test.skip=true clean package

FROM openjdk:8-jre-alpine3.9

COPY --from=MAVEN_BUILD target/identity-0.0.1-SNAPSHOT.jar /identity.jar

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "/identity.jar"]