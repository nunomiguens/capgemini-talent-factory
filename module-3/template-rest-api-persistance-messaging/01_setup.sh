#!/bin/sh
docker network rm tfnet
docker network create tfnet
docker run -p 5432:5432 --rm --name postgres-server -e POSTGRES_PASSWORD=postgres \
  --network tfnet -d postgres:14.3-alpine
