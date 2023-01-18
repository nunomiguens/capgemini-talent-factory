#!/bin/sh
docker stop tomcat-container
docker run -p 8082:8080 -p 2223:22 -d -it --rm --name tomcat-container --mount type=bind,source="$(pwd)",target=/app tomcat-image
