#!/bin/sh
QUEUE_NAME=template-cloud-queue
awslocal sqs send-message --queue-url \
  http://localhost:4566/000000000000/$QUEUE_NAME \
  --message-body "$1"

