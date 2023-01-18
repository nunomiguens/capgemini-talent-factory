#!/bin/sh
docker stop zookeeper-server kafka-server postgres-server activemq-server-container
docker network rm tfnet
docker network create tfnet
docker run -p 5432:5432 --rm --name postgres-server -e POSTGRES_PASSWORD=postgres \
  --network tfnet -d postgres:14.3-alpine
docker run -p 2181:2181 -p 2888:2888 -p 3888:3888 --rm -d --name zookeeper-server --network tfnet \
  -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest
docker run -p 9092:9092 --rm -d --name kafka-server --network tfnet \
  -e ALLOW_PLAINTEXT_LISTENER=yes  \
  -e  KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper-server:2181 bitnami/kafka:latest
cd active-mq-server
docker build -t activemq-server-image .
docker run -d --rm -p 8161:8161 -p 61616:61616 -p 5672:5672 -p 61613:61613 \
  -p 1883:1883 -p 61614:61614 --name activemq-server-container \
  --network tfnet activemq-server-image
cd ..
