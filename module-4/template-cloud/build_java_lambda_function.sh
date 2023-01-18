#!/bin/sh
FUNCTION_NAME=templatecloud-lambda-function
QUEUE_NAME=template-cloud-queue
awslocal lambda delete-function --function-name $FUNCTION_NAME
sleep 5
awslocal sqs delete-queue --queue-url \
  http://localhost:4566/000000000000/$QUEUE_NAME
sleep 5
mvn clean install
sleep 5
awslocal lambda create-function --function-name $FUNCTION_NAME \
  --zip-file fileb://target/$FUNCTION_NAME-1.0-SNAPSHOT.jar \
  --handler com.capgemini.pt.talentfactory.templatecloud.TemplateCloudLambdaFunction \
  --runtime java8 \
  --role arn:aws:iam::000000000000:role/lambda-ex --timeout 30
#awslocal lambda update-function-configuration --function-name $FUNCTION_NAME \
#  --timeout 15
sleep 15

awslocal sqs create-queue --queue-name $QUEUE_NAME
awslocal lambda create-event-source-mapping --function-name $FUNCTION_NAME  \
  --batch-size 1 \
  --event-source-arn arn:aws:sqs:us-east-1:000000000000:$QUEUE_NAME 

