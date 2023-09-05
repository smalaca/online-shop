FROM maven:3.8.3-openjdk-17 as builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package

FROM openjdk:17-alpine
COPY --from=builder target/product-management-1.0-SNAPSHOT.jar product-management-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","product-management-1.0-SNAPSHOT.jar"]