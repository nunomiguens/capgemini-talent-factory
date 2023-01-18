#!/bin/sh
docker stop postgres-server
docker run -p 5432:5432 --rm --name postgres-server -e POSTGRES_PASSWORD=postgres \
  --network tfnet -d postgres:14.3-alpine
sleep 15
docker cp setup.sql postgres-server:/tmp/
docker exec -it postgres-server psql --username postgres -d postgres -f /tmp/setup.sql 
docker exec -it postgres-server psql --username postgres -d postgres -c 'select * from pagamentoFaturas;'
