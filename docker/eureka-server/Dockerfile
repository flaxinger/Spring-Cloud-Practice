FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY eureka-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8070
ENTRYPOINT ["java","-jar","/app.jar"]