#!/bin/sh 
awslocal lambda invoke --function-name templatecloud-lambda-function \
  --payload '{ "first_name": "Bob","last_name":"Marley" }' response.json 
cat response.json
