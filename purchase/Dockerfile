FROM online-shop/online-shop-schema:latest as builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package

FROM openjdk:17-alpine
COPY --from=builder target/purchase-1.0-SNAPSHOT.jar purchase-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","purchase-1.0-SNAPSHOT.jar"]