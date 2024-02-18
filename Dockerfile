FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/StarlightOnlineStore-0.0.1-SNAPSHOT.jar starlightOnlineStore.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/starlightOnlineStore.jar"]






#ADD target/starlightOnlineStore-docker.jar starlightOnlineStore-docker.jar
#ENTRYPOINT ["java", "-jar", "/starlightOnlineStore-docker.jar"]