FROM eclipse-temurin:21-jdk

WORKDIR /rococo
ENV TZ=Europe/Moscow
COPY ./gradle ./gradle
COPY ./rococo-e-2-e-tests ./rococo-e-2-e-tests
COPY ./rococo-grpc-common ./rococo-grpc-common
COPY ./gradlew ./
COPY ./build.gradle ./
COPY ./settings.gradle ./
COPY ./gradle.properties ./

ENV BROWSER=${BROWSER}

CMD ./gradlew test -Dtest.env=docker -Duser.timezone=Europe/Moscow
