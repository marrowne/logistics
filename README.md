# logistics
A simple Spring-based parcel tracking system as application of DDD. It contains 3 bounded contexts:
- Tracking
- HR
- Identity

Stack:
- Spring Boot
- H2/MySQL
- Liquibase
- React-Admin
- Spring Security OAuth
- REST Assured
- JUnit + Mockito
- GSON

Running as dev maven profile:

`mvn spring-boot:run -f <context>/pom.xml`, executed in repo directory where _\<context>_ is any of bounded contexts above written in lowercase.

To run React-Admin it is required to set env variable `export REACT_APP_STAGE="dev"` and run `yarn start` in _logistics-panel_ directory.

In common directory are prepared Terraform scripts to set up Jenkins and Kubernetes on AWS relying on Dockerfiles created for all apps.