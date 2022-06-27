FROM openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY bank-transaction-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar","--DATABASE_ADDRESS=BT-mysql", "--EUREKA_ADDRESS=BT-eureka","--STATIC_DIRECTORY=/home/static","--EUREKA_HOSTNAME=BT-api"]