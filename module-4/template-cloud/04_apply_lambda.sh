#!/bin/sh
awslocal sqs create-queue --queue-name update-dyndb-queue

zip py-my-function.zip lambda_function.py
awslocal lambda delete-function --function-name py-my-function
awslocal lambda create-function --function-name py-my-function \
  --zip-file fileb://py-my-function.zip \
  --handler lambda_function.lambda_handler  \
  --runtime python3.9 \
  --role arn:aws:iam::000000000000:role/lambda-ex

awslocal lambda create-event-source-mapping --function-name py-my-function  \
  --batch-size 1 \
  --event-source-arn arn:aws:sqs:us-east-1:000000000000:update-dyndb-queue 

