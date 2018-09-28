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
EXPOSE 8080
RUN apt-get update && \
    apt-get install -y curl bash && \
    rm -rf /var/lib/apt/lists/*
COPY ./docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
COPY ./docker/fscMap.txt /hazard-ds/fscMap.txt
COPY ./docker/fsgMap.txt /hazard-ds/fsgMap.txt
COPY --from=builder /usr/app/app.jar /opt/app.jar
ENTRYPOINT ["/docker-entrypoint.sh"]
