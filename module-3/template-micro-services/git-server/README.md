# Build and run 

```
sh build.sh
sh run.sh 
```

After a ```docker ps``` command we should see the container git-server-container.

# Set SSH pub key in authorized_keys

```
docker cp ~/.ssh/id_rsa.pub git-server-container:/tmp
docker exec -it git-server-container cat /tmp/id_rsa.pub
docker exec -it git-server-container sh -c 'cat /tmp/id_rsa.pub >> /home/git/.ssh/authorized_keys'
docker exec -it git-server-container sh -c 'cat /home/git/.ssh/authorized_keys'
```

# Initial steps on the repository

```
rm /home/vagrant/.ssh/known_hosts 
rm -rf ~/tmp/hello
mkdir ~/tmp
cd ~/tmp
rm -rf hello/
git clone ssh://git@127.0.0.1:2222/home/git/hello.git 
# user:pwd git:git 
cd hello 
git config user.email "vagrant@talentfactory.capgemini.com"
git config user.name "AwesomeDev"
touch README
git add README
git commit -m 'initial commit'
git push origin master
echo -e "stuff 1234\n" >> README
git add README 
git commit -m "other commit"
git pull origin master
git push origin master 
git log 
```

# Create simple Java console program

```
cd ~/tmp/hello 
touch pom.xml 
mkdir -p src/main/java/com/capgemini/talentfactory/
touch src/main/java/com/capgemini/talentfactory/JavaHelloWorld.java
```

## pom.xml
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.capgemini.talentfactory</groupId>
    <artifactId>java-hello-world</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <build>
      <plugins>
        <plugin>
          <artifactId>maven-assembly-plugin</artifactId>
          <version>2.4</version>
          <configuration>
            <descriptorRefs>
              <descriptorRef>jar-with-dependencies</descriptorRef>
           </descriptorRefs>
           <archive>
             <manifest>
               <mainClass>com.capgemini.talentfactory.JavaHelloWorld</mainClass>
             </manifest>
           </archive>
          </configuration>
          <executions>
            <execution>
              <id>make-assembly</id> 
              <phase>package</phase> 
              <goals>
                <goal>single</goal>
              </goals>
            </execution>
          </executions>
          </plugin>
           <plugin>
              <artifactId>maven-compiler-plugin</artifactId>
              <configuration>
                <source>1.8</source>
                <target>1.8</target>
              </configuration>
            </plugin>
      </plugins>
    </build>
</project>
```

## JavaHelloWorld.java 

```
package com.capgemini.talentfactory;

public class JavaHelloWorld {
  public static void main(String[] args){
    System.out.println("TalentFactory - Hello world!");
  }
}
```

# Build and commit code

```
mvn clean install 
java -jar target/java-hello-world-0.0.1-SNAPSHOT-jar-with-dependencies.jar
git add pom.xml src/
git commit -m "0.0.1 SNAPSHOT"
git pull origin master
git push origin master 
git log 
```
