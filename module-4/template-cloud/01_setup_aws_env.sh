#!/bin/sh
docker stop localstack_container
docker network rm tfnet
docker network create tfnet
docker run -d --name localstack_container --rm -it -p 4566:4566 -p 4571:4571 --network tfnet  localstack/localstack
