#!/bin/sh
awslocal dynamodb delete-table --table-name PagamentoFaturas
awslocal dynamodb create-table --table-name PagamentoFaturas \
--attribute-definitions AttributeName=numeroFatura,AttributeType=S AttributeName=descricaoFatura,AttributeType=S  \
--key-schema AttributeName=descricaoFatura,KeyType=HASH AttributeName=numeroFatura,KeyType=RANGE   \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

awslocal dynamodb put-item --table-name PagamentoFaturas --item '{"numeroFatura":{"S":"O1"},"descricaoFatura":{"S":"Lorem Ipsum is simply dummy text of the"}}'
awslocal dynamodb put-item --table-name PagamentoFaturas --item '{"numeroFatura":{"S":"O2"},"descricaoFatura":{"S":"Lorem Ipsum is simply dummy text of the"} , "estadoFatura":{"S":"Registada"}    }'
awslocal dynamodb put-item --table-name PagamentoFaturas --item '{"numeroFatura":{"S":"O3"},"descricaoFatura":{"S":"Lorem Ipsum is simply dummy text of the"} , "estadoFatura":{"S":"Registada"}    }'
awslocal dynamodb put-item --table-name PagamentoFaturas --item '{"numeroFatura":{"S":"O4"},"descricaoFatura":{"S":"Lorem Ipsum is simply dummy text of the"}  }'
awslocal dynamodb put-item --table-name PagamentoFaturas --item '{"numeroFatura":{"S":"O5"},"descricaoFatura":{"S":"Lorem Ipsum is simply dummy text of the"} , "estadoFatura":{"S":"Registada"}    }'
