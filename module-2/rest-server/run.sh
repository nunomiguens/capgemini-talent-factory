#!/bin/sh
mvn clean install
docker build -t rest-server-image .
docker stop rest-server-container
docker run -p 8080:8080 --rm --name rest-server-container --network tfnet rest-server-image
