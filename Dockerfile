FROM openjdk:17

COPY target/pixels-userservice-1.jar pixels-userservice.jar

EXPOSE 8102

ENTRYPOINT ["java","-jar","pixels-userservice.jar"]
