FROM maven:3.8.3-openjdk-17

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package
RUN mvn install:install-file -Dfile=target/annotations-ddd-1.0-SNAPSHOT.jar \
    -DgroupId=com.smalaca  \
    -DartifactId=annotations-ddd  \
    -Dversion=1.0-SNAPSHOT  \
    -Dpackaging=jar