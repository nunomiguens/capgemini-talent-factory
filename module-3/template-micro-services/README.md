# Steps

## Add hard disk (optional)

```sh
vagrant halt 
# virtualbox
# vm
# settings
# storage, add hard disk 
# create 
# vdi, next, fixed, 16GB , create 
# Not attached, choose 
# ok 
vagrant up 
sudo cfdisk /dev/sdb 
# gpt 
# free space, new, 16gb 
# write, yes, quit 
sudo mkfs.ext4 /dev/sdb1
mkdir ~/Documents
sudo nano /etc/fstab
/dev/sdb1 /home/vagrant/Documents ext4 rw 0 0
vagrant reload 
cd Documents/
sudo chown vagrant . -R
sudo chgrp vagrant . -R
touch a 
nano /etc/fstab

/dev/sdb1 /home/vagrant/Documents ext4 rw 0 0 

docker system prune -a --volumes 
```



## Setup git server

```sh
cd git-server
sh 01_build.sh 
sh 02_run.sh 
sh 03_set_ssh_keys.sh 
# Generating public/private rsa key pair.
# Enter file in which to save the key (/home/vagrant/.ssh/id_rsa): 
# Enter passphrase (empty for no passphrase): 
# Enter same passphrase again: 
sh 04_get_repository.sh 
# Are you sure you want to continue connecting (yes/no/[fingerprint])? yes
sh 05_push_code_rest_consumer.sh
docker ps 
```

## Setup jenkins 

```sh
# optional
sudo systemctl enable jenkins 
sudo systemctl start jenkins 
# optional
cd /vagrant/template-micro-services/jenkins
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
# 8ebea839c421449cb468f784f588bfc9
# http://localhost:8080/
# paste 
# continue 
# install suggested plugins 
# user jenkins 
# pwd jenkins 
# fullname jenkins 
# email my.user@capgemini.com
# save and finish 
# start using jenkins 
```

```sh
sudo bash 
su jenkins 
cd ~
ssh-keygen 
# Generating public/private rsa key pair.
# Enter file in which to save the key (/var/lib/jenkins/.ssh/id_rsa): 
# Created directory '/var/lib/jenkins/.ssh'.
# Enter passphrase (empty for no passphrase): 
exit 
exit 

sudo cp /var/lib/jenkins/.ssh/id_rsa.pub ~/tmp
sudo chmod 755 ~/tmp/id_rsa.pub 
docker cp ~/tmp/id_rsa.pub git-server-container:/tmp
docker exec -it git-server-container sh -c \
  'cat /tmp/id_rsa.pub >> /home/git/.ssh/authorized_keys' 
docker exec -it git-server-container sh -c 'cat /home/git/.ssh/authorized_keys'

sudo bash
su jenkins 
cd ~
ssh-keygen -f "/var/lib/jenkins/.ssh/known_hosts" -R "[localhost]:2222"
git ls-remote -h -- ssh://git@localhost:2222/home/git/hello.git HEAD 
# Are you sure you want to continue connecting (yes/no/[fingerprint])? yes
exit 
exit 
sudo usermod -a jenkins -G docker
sudo service jenkins restart

# In firefox go to http://localhost:8080/
# new item 
# freestyle project 
# build-rest 
# ok 
# source code management: git 
# repository url: ssh://git@localhost:2222/home/git/hello.git
# build steps
# add build step 
# execute shell
# cd rest-server && mvn clean install
# apply save 

# jenkins dashboard
# new item 
# build-consumer 
# freestyle project 
# ok 
# source code management Git 
# repository url: git ssh://git@localhost:2222/home/git/hello.git
# build steps 
# add build step 
# execute shell 
# cd consumer && mvn clean install
# apply save 


```

## archiva 

```sh
cd archiva
sh build.sh 
sh run.sh
# open http://localhost:8082/manager/html admin:12345678
# open http://localhost:8082/archiva/
# click in "create admin user" 
# admin:admin1 test@example.org 
# validated, click on save 

# Goto http://localhost:8082/archiva/#users (Manage users)
# Users list, Add 
# Create user userx:userx1 full name userx with email user@example.org  , validated, save
# User userx, edit roles
# Registered user
# Repository manager
#  - snapshots
#  - internal
# Update

sudo bash
su jenkins
cd ~
mkdir -p ~/.m2/
nano ~/.m2/settings.xml
<settings>
  <servers>
    <server>
      <id>archiva.internal</id>
      <username>userx</username>
      <password>userx1</password>
    </server>
    <server>
      <id>archiva.snapshots</id>
      <username>userx</username>
      <password>userx1</password>
    </server>
  </servers>
</settings>
exit 
exit 

cd ~/tmp/hello/consumer 
nano pom.xml
    <!-- archiva repositories in pom.xml before </project> -->
    <distributionManagement>
        <repository>
          <id>archiva.internal</id>
          <name>Internal Release Repository</name>
          <url>http://localhost:8082/archiva/repository/internal/</url>
        </repository>
        <snapshotRepository>
          <id>archiva.snapshots</id>
          <name>Internal Snapshot Repository</name>
          <url>http://localhost:8082/archiva/repository/snapshots/</url>
        </snapshotRepository>
    </distributionManagement>
</project>

cd ~/tmp/hello/rest-server 
nano pom.xml
    <!-- archiva repositories in pom.xml before </project> -->
    <distributionManagement>
        <repository>
          <id>archiva.internal</id>
          <name>Internal Release Repository</name>
          <url>http://localhost:8082/archiva/repository/internal/</url>
        </repository>
        <snapshotRepository>
          <id>archiva.snapshots</id>
          <name>Internal Snapshot Repository</name>
          <url>http://localhost:8082/archiva/repository/snapshots/</url>
        </snapshotRepository>
    </distributionManagement>
</project>
cd ..
git diff 
# Add -SNAPSHOT to version in pom.xml of consumer and rest projects
# <version>0.1.0-SNAPSHOT</version>
nano consumer/pom.xml 
nano rest-server/pom.xml

git add consumer/pom.xml rest-server/pom.xml 
git commit -m "added SNAPSHOT to version and artifact repos"
git pull origin master
git push origin master
git log

# http://localhost:8080/job/build-rest/configure
# Add mvn deploy in jenkins jobs (rest and consumer)
# start jobs for build-rest and build-consumer 
# http://localhost:8082/archiva/#artifact/com.capgemini.pt.talentfactory/rest
# http://localhost:8082/archiva/#artifact/com.capgemini.pt.talentfactory/consumer
```

## registry
```sh
cd registry
sh run.sh

# update Dockerfile of consumer and rest to have -SNAPSHOT.jar in CMD and COPY 
cd ~/tmp/hello/
nano consumer/Dockerfile
nano rest-server/Dockerfile
git diff
git add consumer/Dockerfile rest-server/Dockerfile 
git commit -m "updated Dockfefile's"
git pull origin master
git push origin master

# update jenkins job build-consumer 
# http://localhost:8080/job/build-consumer/configure
# build steps 
cd consumer
/usr/bin/mvn clean install
/usr/bin/mvn deploy
echo "testpassword" > pwd.txt
cat pwd.txt | /usr/bin/docker login localhost:5000 --username testuser --password-stdin
/usr/bin/docker build -t consumer-image .
/usr/bin/docker tag consumer-image localhost:5000/consumer-image:0.1
/usr/bin/docker push localhost:5000/consumer-image:0.1
# apply save 

# update jenkins job rest-consumer
# http://localhost:8080/job/build-rest/configure
# build steps 
cd rest-server
/usr/bin/mvn clean install
/usr/bin/mvn deploy
echo "testpassword" > pwd.txt
cat pwd.txt | /usr/bin/docker login localhost:5000 --username testuser --password-stdin
/usr/bin/docker build -t rest-server-image .
/usr/bin/docker tag rest-server-image localhost:5000/rest-server-image:0.1
/usr/bin/docker push localhost:5000/rest-server-image:0.1
# apply save 

# run both jobs rest and consumer 
# check registry catalog  
curl "http://testuser:testpassword@localhost:5000/v2/_catalog"

```

## k3s - kubernetes
```sh
cd active-mq-server
sh push_to_registry.sh
cd ../k3s
docker system prune -a --volumes 
sudo apt autoremove 
df -h 

sudo service k3s restart
sudo chmod 740 /etc/rancher/k3s/k3s.yaml
kubectl get secrets
kubectl delete secret  docker-registry-creds
kubectl create secret docker-registry docker-registry-creds \
  --docker-server="localhost:5000" --docker-username=testuser \
  --docker-password=testpassword

docker system prune -a --volumes 
kubectl apply -f activemq.yaml 
kubectl get pods
sleep 90

kubectl apply -f postgresql.yaml 
sleep 90
kubectl get pods

kubectl apply -f zookeeper.yaml 
sleep 90
kubectl get pods

kubectl apply -f kafka.yaml 
sleep 90
kubectl get pods

kubectl apply -f consumer.yaml 
sleep 90
kubectl get pods

kubectl apply -f rest.yaml 
sleep 90
kubectl get pods

kubectl get services
kubectl get deployments
kubectl get pods

REST_IP=$(kubectl get services  | grep rest  | awk '//{print $3}')
echo $REST_IP
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/invoice --data '{"name":"ix1","items":[{"name":"a1a","value":1.1},{"name":"a2b","value":1.2},{"name":"a3c","value":1.3}]}'
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/invoice --data '{"name":"ix2","items":[{"name":"a1","value":1.1},{"name":"a2","value":1.2},{"name":"a3","value":1.3}]}'
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/invoice --data '{"name":"ix3","items":[{"name":"B1","value":1.11},{"name":"B2","value":1.22},{"name":"B3","value":1.33}]}'

curl $REST_IP:8080/invoice/ix1
curl $REST_IP:8080/invoice/ix2
curl $REST_IP:8080/invoice/ix3
curl $REST_IP:8080/itemLowercase/ix3
curl $REST_IP:8080/invoice/ix3
curl $REST_IP:8080/itemUppercase/ix1
curl $REST_IP:8080/invoice/ix1
curl $REST_IP:8080/itemLowercase/ix1
curl $REST_IP:8080/invoice/ix1
curl $REST_IP:8080/itemUppercase/ix2
# connect to postgresql service 
POSTGRES_IP=$(kubectl get service | grep postgres | awk '//{print $3}')
psql -h $POSTGRES_IP -U postgres # pwd postgres
\c postgres
\dt
select * from invoice;
\q

```

## Remove k3s services and deployments (optional)
```sh
kubectl delete deployment activemq-server-container kafka-server zookeeper-server postgres-server consumer rest
kubectl delete services   activemq-server-container kafka-server zookeeper-server postgres-server consume rest
```
