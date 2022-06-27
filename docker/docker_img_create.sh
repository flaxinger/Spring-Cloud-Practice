#!/bin/bash

HOME=$(pwd)

cd ./../bank-transaction
./mvnw clean
./mvnw package -DskipTests
cd $HOME/bank-transaction
cp ./../../bank-transaction/target/bank-transaction-0.0.1-SNAPSHOT.jar .
docker build -t flaxinger/bt-api .
cd ..


cd ./../eureka
./mvnw clean
./mvnw package -DskipTests
cd $HOME/eureka-server
cp ./../../eureka/target/eureka-0.0.1-SNAPSHOT.jar .
docker build -t flaxinger/bt-eureka .
cd ..


cd ./../hystrix-loadbalancer
./mvnw clean
./mvnw package -DskipTests
cd $HOME/hystrix-loadbalancer
docker build -t flaxinger/bt-hystrix .
cp ./../../hystrix-loadbalancer/target/hystrix-loadbalancer-0.0.1-SNAPSHOT.jar .
cd ..