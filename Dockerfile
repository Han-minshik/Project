#FROM openjdk:21
#COPY target/project-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8080:80
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:21
COPY target/project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]