docker build -t activemq-server-image .
echo "testpassword" > pwd.txt
cat pwd.txt | /usr/bin/docker login localhost:5000 --username testuser --password-stdin
docker tag activemq-server-image  localhost:5000/activemq-server-image:0.1
docker push localhost:5000/activemq-server-image:0.1
