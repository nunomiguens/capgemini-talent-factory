# Steps
```sh
mvn clean install
docker build -t consumer-image .
docker stop consumer-container
docker run --rm --name consumer-container --network tfnet consumer-image 
```
