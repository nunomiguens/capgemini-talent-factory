import boto3
import os

def lambda_handler(event, context):
    session = boto3.session.Session()

    print(os.environ["AWS_ACCESS_KEY_ID"])
    print(os.environ["AWS_SECRET_ACCESS_KEY"])

    client = session.client(
        service_name='dynamodb',
        aws_access_key_id=os.environ["AWS_ACCESS_KEY_ID"],
        aws_secret_access_key=os.environ["AWS_SECRET_ACCESS_KEY"],
        endpoint_url='http://localhost:4566',
    )

    body={'dyntables':[]}
    for table in  client.list_tables()['TableNames']:
        body['dyntables'].append(table)

    payload = ''
    if 'Records' in event:
        for record in event['Records']:
            body['msg'] = record['body']
            payload= record['body']

    response = client.update_item(
        TableName='Entity',
        Key={
            'cname': {'S':'O5'},
            'ctype': {'S':'SHAPE'}
        },
        UpdateExpression="SET cstate = :newval",
        ExpressionAttributeValues={':newval': {'S':payload} },
        ReturnValues="UPDATED_NEW"
    )

    return body

