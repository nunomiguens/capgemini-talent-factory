#!/bin/sh
mvn clean install
docker build -t consumer-image .
docker stop consumer-container
docker run -d --rm --name consumer-container --network tfnet consumer-image
