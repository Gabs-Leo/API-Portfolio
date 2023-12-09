FROM maven:3-openjdk-18 as build
COPY . .
RUN mvn clean install -Dmaven.test.skip

FROM openjdk
COPY --from=build target/*.jar app.jar
COPY ./src/main/resources ./src/main/resources
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]