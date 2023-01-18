#!/bin/sh
docker exec -it postgres-server psql --username postgres \
  -d postgres -c "update entity set cstate='$1' where cname='05';"
awslocal sqs send-message --queue-url http://localhost:4566/000000000000/update-dyndb-queue \
  --message-body "$1"
