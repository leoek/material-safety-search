FROM openjdk:10.0.1-10-jdk-sid as builder
RUN mkdir -p /usr/app/src
RUN apt-get update && \
    apt-get install -y gradle && \
    rm -rf /var/lib/apt/lists/*
COPY ./gradle /usr/app/gradle
COPY ./gradlew /usr/app
COPY ./build.gradle /usr/app
WORKDIR /usr/app
RUN ./gradlew downloadDependencies
COPY ./src /usr/app/src
RUN ./gradlew build
RUN cp /usr/app/build/libs/*.jar /usr/app/app.jar

FROM openjdk:10.0.1-10-jre-slim-sid
COPY --from=builder /usr/app/app.jar /opt/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/opt/app.jar"]
EXPOSE 8080
