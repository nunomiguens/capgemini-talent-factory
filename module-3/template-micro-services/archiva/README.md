# Setup 

```
sh build.sh 
sh run.sh
```

# Access Tomcat 

Apache Tomcat admin http://localhost:8082/manager/html
User and passwd admin:12345678

# Archiva 

Access http://localhost:8082/archiva/
create admin user 
admin:admin1 test@example.org 
validated, click on save 

Goto http://localhost:8082/archiva/#users (Manage users)
Users list, Add 
Create user userx:userx1 full name userx with email user@example.org  , validated, save

User userx, edit roles
Registered user
Repository manager
 - snapshots
 - internal
Update

Configure maven settings for jenkins user
```
sudo bash
su jenkins
/var/lib/jenkins/.m2
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
```

# Add repositories to projects pom.xml
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

# Add snapshot to version 

The <version> of the pom.xml projects must have -SNAPSHOT so that they can be redeployed with ```mvn deploy```

# Add mvn deploy to jenkins jobs 

```
mvn clean install 
mvn deploy 
```



