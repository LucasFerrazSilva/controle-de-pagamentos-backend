FROM maven:3.8.5-openjdk-17 as build
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
ADD . /usr/src/app
RUN mvn install

FROM eclipse-temurin:17-jre-alpine
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY --from=build /usr/src/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]