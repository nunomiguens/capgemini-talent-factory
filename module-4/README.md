# AWS Cloud using LocalStack
```
- Creation of a Lambda Function for an "Invoice Payment" example
- The lambda function listens to the Queue and alters the database in DynamoDB
- The DynamoDB and Postgresql database are pre-populated with data from invoices on which you can change state
- The Invoice state is updated simultaneously when the message is sended for the SQS
```

# Steps

## ![](https://img.icons8.com/color/19/null/gitlab.png) Setup project 

```sh
cd ~
git clone https://github.com/nunomiguens/capgemini-talent-factory.git
cd module-4/template-cloud
```

## ![](https://img.icons8.com/color/19/null/amazon-web-services.png) Setup LocalStack 
```
docker system prune -a -f --volumes
echo -e "\nPATH=\$PATH:~/.local/bin\n" >> ~/.bashrc
source ~/.bashrc # reload settings including PATH
pip3 install localstack
pip3 install awscli
pip3 install awscli-local
```

## ![](https://img.icons8.com/color/19/null/java-coffee-cup-logo--v1.png) Java steps
```sh
docker stop postgres-server localstack_container
docker rm  postgres-server localstack_container
sh 01_setup_aws_env.sh
curl http://localhost:4566/health | json_pp
sh 02_setup_dynamodb.sh
sh 03_setup_postgresql.sh
sh 06_show_table_entity.sh
sh build_java_lambda_function.sh
sh send_message_java.sh '{ "numeroFatura": "O5","descricaoFatura":"Lorem Ipsum is simply dummy text of the", "estadoFatura":"Em processamento" }'
sh show_java_latest_log.sh
sh 06_show_table_entity.sh
sh send_message_java.sh '{ "numeroFatura": "O5","descricaoFatura":"Lorem Ipsum is simply dummy text of the", "estadoFatura":"Paga" }'
sh show_java_latest_log.sh
sh 06_show_table_entity.sh
```

