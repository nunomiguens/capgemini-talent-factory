# Micro services
- Assembling a pipeline capable of creating an image for the application
developed in module 2 and the Postgres server running from
kubernetes (k3s).
- The rest project Jar file has been made available on Archiva.
- The rest project's Docker image has been made available on
docker registry.

# Steps

## ![](https://img.icons8.com/color/19/null/gitlab.png) Setup project 

```sh
cd ~
git clone https://gitlab.altran.pt/talentfactory/javabackend/nuno-filipe.miguens.git
cd nuno-filipe.miguens
git checkout module-3
cd template-micro-services
docker ps
docker stop registry tomcat-container git-server-container
docker system prune -a
```

## ![](https://img.icons8.com/color/19/null/git.png) Setup git server

```sh
cd template-micro-services/git-server
sh 01_build.sh 
sh 02_run.sh 
sh 03_set_ssh_keys.sh 
sh 04_get_repository.sh 
sh 05_push_code_rest_consumer.sh
docker ps 
```

## ![](https://img.icons8.com/color/19/null/jenkins.png) Setup jenkins 

```sh   
cd /template-micro-services/jenkins
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
Go to http://localhost:8080/
- Paste the password to the textbox > continue 
- install suggested plugins 
- user: jenkins
- password: jenkins 
- fullname: jenkins 
- email: my.user@capgemini.com
- Additional browser support
- Add more integrations

```sh
sudo bash 
su jenkins 
cd ~
ssh-keygen 
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
exit 
exit 
sudo usermod -a jenkins -G docker
sudo service jenkins restart
```
Go to http://localhost:8080/
- new item > name: build-rest > freestyle project > ok
- In source code management > git > repository url: ssh://git@localhost:2222/home/git/hello.git
- In build steps > add build step > execute shell > copy and paste: cd rest-server && mvn clean install
- apply > save 
- run build-rest job 


## ![](https://img.icons8.com/color/19/null/archive-folder.png) Setup Archiva 

```sh
cd template-micro-services/archiva
sh build.sh 
sh run.sh
```
Go to http://localhost:8082/manager/html 
  - user: admin | Password: 12345678 

Go to http://localhost:8082/archiva/
- click in "create admin user" 
- user: admin | Password: admin1 | Email: test@example.org
- Check on validated > click on save 

Go to http://localhost:8082/archiva/-users 
- Users list > Add > Create user
- Username: userx | Full Name: userx | Password: userx1 | Email: user@example.org
- Check on validated > click on save 

On "userx" click in edit roles
- Registered user
- Repository manager > Check snapshots > Check internal
- Update
```
sudo bash
su jenkins
cd ~
mkdir -p ~/.m2/
nano ~/.m2/settings.xml
```
Copy and paste the following code
```
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
```
Ctrl+O > Enter > Ctrl+X
```
exit 
exit 
cd ~/tmp/hello/rest-server 
nano pom.xml
```
Copy and paste the following code before ```</project>```
```
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
```
Ctrl+O > Enter > Ctrl+X
```
cd ..
git diff 
nano rest-server/pom.xml
```
Add -SNAPSHOT to version in pom.xml of rest project: 
```<version>0.2.0-SNAPSHOT</version>```
```
git add rest-server/pom.xml 
git commit -m "added SNAPSHOT to version and artifact repos"
git pull origin master
git push origin master
git log
```
Go to http://localhost:8080/job/build-rest/configure
- In Build Steps add "mvn deploy" in jenkins jobs (rest)
- Start job for build-rest

Go to http://localhost:8082/archiva/#artifact/com.capgemini.pt.talentfactory/rest
- Confirm if "0.2.0-SNAPSHOT" has been added to Versions


## ![](https://img.icons8.com/color/19/null/registry-editor.png) Setup Registry
```sh
cd template-micro-services/registry
sh run.sh  
cd ~/tmp/hello/
nano rest-server/Dockerfile
```
Update Dockerfile of rest to have -SNAPSHOT.jar in CMD and COPY
```
git diff
git add rest-server/Dockerfile 
git commit -m "updated Dockfefile's"
git pull origin master
git push origin master
```
Go to http://localhost:8080/job/build-rest/configure
- In Build Steps replace the existing for:
```
cd rest-server
/usr/bin/mvn clean install
/usr/bin/mvn deploy
echo "testpassword" > pwd.txt
cat pwd.txt | /usr/bin/docker login localhost:5000 --username testuser --password-stdin
/usr/bin/docker build -t rest-server-image .
/usr/bin/docker tag rest-server-image localhost:5000/rest-server-image:0.2
/usr/bin/docker push localhost:5000/rest-server-image:0.2
```
- apply- > save 
- run job rest
- check registry catalog  
```
curl "http://testuser:testpassword@localhost:5000/v2/_catalog"
```

## ![](https://img.icons8.com/color/19/null/kubernetes.png) Setup k3s - kubernetes
```sh
cd template-micro-services/k3s
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
sleep 90
kubectl get pods

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
```

# Testes ![](https://img.icons8.com/color/19/null/source-code.png)
## Criar Restaurantes
```
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/criaRestaurante --data '{"nome":"Taska da Estação", "morada":"Rua em Cima Debaixo", "codigoPostal":"5500-221"}'
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/criaRestaurante --data '{"nome":"Nham Nham", "morada":"Rua do Outro Lado", "codigoPostal":"7350-666"}'
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/criaRestaurante --data '{"nome":"Oligarca", "morada":"Av do Castelo Preto", "codigoPostal":"1978-123"}'
```

## Criar Menus
```
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/criaMenu/Nham%20Nham --data '{"nome":"Menu Segunda","descricao":"As descrições devem ser calorosas e transmitir a alma do restaurante. Elas precisam cativar o cliente e ...","opcaoMenu":[{"carne":"Bife Tártaro", "peixe":"Bacalhau Polo Norte"}]}'
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/criaMenu/Oligarca --data '{"nome":"Menu Quarta","descricao":"As descrições devem ser calorosas e transmitir a alma do restaurante. Elas precisam cativar o cliente e ...","opcaoMenu":[{"carne":"Frango Esturricado", "peixe":"Salmão Branco"}]}'
```

## Ver Restaurantes
```
curl $REST_IP:8080/listaRestaurantes
```

## Ver Menus
```
curl $REST_IP:8080/listaMenus/Nham%20Nham
curl $REST_IP:8080/listaMenus/Oligarca
```

## Criar Encomendas
```
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/criaEncomenda/Oligarca --data '["Menu Quarta"]'
curl -X POST -H 'Content-Type: application/json' $REST_IP:8080/criaEncomenda/Nham%20Nham --data '["Menu Segunda"]'
```

## Ver Encomenda
```
curl $REST_IP:8080/listarEncomenda/1
curl $REST_IP:8080/listarEncomenda/2
```

## Processa Encomenda
```
curl -X POST $REST_IP:8080/processaEncomenda/1
curl -X POST $REST_IP:8080/processaEncomenda/2
```

## Ver Histórico Encomendas
```
curl $REST_IP:8080/historicoEncomenda
```

