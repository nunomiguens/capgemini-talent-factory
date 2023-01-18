#!/bin/sh
awslocal dynamodb scan --table-name PagamentoFaturas
docker exec -it postgres-server psql --username postgres -d postgres -c 'select * from pagamentoFaturas;'

