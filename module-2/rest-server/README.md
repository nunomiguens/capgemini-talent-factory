# Build jar
```
mvn clean install 
```

# Run jar
```
java -jar target/rest-0.1.0.jar
```

# Build image
```
docker build -t rest-server-image .
```

# Run container
```
docker run -p 8080:8080 --rm --name rest-server-container --network tfnet rest-server-image
```



