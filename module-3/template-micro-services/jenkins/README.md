# Install Jenkins 2.332.3 LTS

```
sudo curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
sudo sh -c 'echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/ | sudo tee  /etc/apt/sources.list.d/jenkins.list > /dev/null '
sudo apt update
sudo apt install fontconfig jenkins -y 
# which services should be restarted, jenkins.service 
netstat -at -n  | grep 8080 # should show jenkins listening on port 8080
```

# Configure Jenkins 

```
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
# open page http://localhost:8080/login?from=%2F
# Paste the password to the textbox , continue 
# Click on Install suggested plugins 
# Create First Admin User
# username: jenkins 
# password: jenkins
# full name: jenkins
# email: my.user@capgemini.com
# Save and continue 
# Jenkins Url: http://localhost:8080/
# Save and finish 
# Jenkins is ready! click on "start using jenkins"

sudo bash 
su jenkins 
cd ~
ssh-keygen  
cat /var/lib/jenkins/.ssh/id_rsa.pub
ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQCz1v8SRzKVSjNZzubZ5yE+zzqalmx9bukupo/0dProtRW6s3Gu6imR7uquYjZwYFjUY3dvK4y8OvHJIn48WI7LkUVRNwXTQx43VuR3eKPHG+ynjVHYLPvvWmRSbdhMlgvYYiOGTVX38vNqrN8RDJ7GkE8AnXdgraAAR4vVx2t5BL9VFrKREiOH3RQHA2J1Qqnr3I3iSwdlPWiaziGuzVP/yTjngf9hPlc3v8mHMLG2I5QpiuTKo1GPWS2tHR1zd1OlyCCZQh9FXQAj4FUVs5ygpWDqtsX2J4qr4aU7gpP8O+wohqbbSQkW49r7yj+2nB36/YpcbXoiUA3fTjxcOqOH8djaYDMGgDDQtyCwUjxuSeLXNrwiLQLOV7NUChunInTDpr8yjnH2/itR8tR7VcFgAgm65a25VzAMprChNFK4WMF7V0INZ7YN8gQmM9cLAdyh7wxqH4ArPaChiYOGzDo5/SRdO1HYtneOfy6hXYgJ8pItj7Tm8lSftvRlsKW1Ipk= jenkins@bullseye
exit
exit 
sudo cp /var/lib/jenkins/.ssh/id_rsa.pub ~/tmp/
sudo chmod 755 ~/tmp/id_rsa.pub 
docker cp ~/tmp/id_rsa.pub git-server-container:/tmp

docker exec -it git-server-container sh -c 'cat /tmp/id_rsa.pub >> /home/git/.ssh/authorized_keys' 
docker exec -it git-server-container sh -c 'cat /home/git/.ssh/authorized_keys'

# ensure it connects with jenkins
sudo bash
su jenkins 
git ls-remote -h -- ssh://git@localhost:2222/home/git/hello.git HEAD 
# continue connecting: yes
exit 

# give user jenkins access to docker
sudo usermod -a jenkins -G docker
sudo service jenkins restart

# http://localhost:8080/
# Dashboard, + New item, "build-rest" freestyle project, ok 
# tab source code management, git , repository url ssh://git@localhost:2222/home/git/hello.git
# credentials, none (using ssh pub key from jenkins user)
# master branch 
# build triggers, poll scm, */1 * * * * 
# add build step, execute shell , 
# cd rest-server
# /usr/bin/mvn clean install
# apply 
# save
# /var/lib/jenkins/workspace/build-rest/rest-server/target/rest-0.1.0.jar

# Dashboard, + New item, "build-consumer" freestyle project, ok 
# tab source code management, git , repository url ssh://git@localhost:2222/home/git/hello.git
# credentials, none (using ssh pub key from jenkins user)
# master branch 
# build triggers, poll scm, */1 * * * * 
# add build step, execute shell , 
# cd consumer
# /usr/bin/mvn clean install
# apply 
# save
# /var/lib/jenkins/workspace/build-consumer/consumer/target/consumer-0.1.0.jar

```
