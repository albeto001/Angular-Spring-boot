FROM openjdk:11
EXPOSE 8080
ADD spring-boot-backend-apirest/target/spring-boot-backend-apirest-0.0.1-SNAPSHOT.jar backEnd.jar
RUN java -jar /backEnd.jar
