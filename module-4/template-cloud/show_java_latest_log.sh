#!/bin/sh
LOG_GROUP="/aws/lambda/templatecloud-lambda-function"
LOG_STREAM=$(awslocal logs describe-log-streams \
  --log-group-name $LOG_GROUP \
  --order-by LastEventTime --descending | \
  grep logStreamName | head -1 | awk '//{print $2}' | sed "s/,//g" | sed 's/\"//g' )
echo $LOG_GROUP
echo $LOG_STREAM
awslocal logs get-log-events --log-group-name $LOG_GROUP \
  --log-stream-name "$LOG_STREAM" \
  | grep message \
  | sed 's/"message"\://g' \
  | sed 's/             //g'

