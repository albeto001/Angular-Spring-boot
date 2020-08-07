FROM openjdk:11
EXPOSE 8080
ADD target/spring-boot-backend-apirest-0.0.1-SNAPSHOT.jar backEnd.jar
ENTRYPOINT ["java", "-jar", "/backEnd.jar"]