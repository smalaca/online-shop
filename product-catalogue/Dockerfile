FROM online-shop/online-shop-schema:latest as builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package

FROM openjdk:17-alpine
COPY --from=builder target/product-catalogue-1.0-SNAPSHOT.jar product-catalogue-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","product-catalogue-1.0-SNAPSHOT.jar"]