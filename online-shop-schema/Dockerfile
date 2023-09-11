FROM maven:3.8.3-openjdk-17

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package
RUN mvn install:install-file -Dfile=target/online-shop-schema-1.0-SNAPSHOT-jar-with-dependencies.jar \
    -DgroupId=com.smalaca  \
    -DartifactId=online-shop-schema  \
    -Dversion=1.0-SNAPSHOT  \
    -Dpackaging=jar