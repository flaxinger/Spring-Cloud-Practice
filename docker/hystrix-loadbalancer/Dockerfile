FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY hystrix-loadbalancer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar","--EUREKA_ADDRESS=BT-eureka","--EUREKA_HOSTNAME=BT-eureka"]