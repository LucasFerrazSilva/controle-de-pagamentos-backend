FROM maven:3.8.5-openjdk-17 AS build
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
ADD . /usr/src/app
ARG DROPBOX_TOKEN
ENV DROPBOX_TOKEN ${DROPBOX_TOKEN}
RUN mvn install

FROM eclipse-temurin:17-jre-alpine
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY --from=build /usr/src/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]