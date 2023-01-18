FROM alpine:3.16
RUN apk add --update --no-cache curl wget nano vim openssh sudo bash gcompat openjdk7 maven 
RUN sed -i s/#PermitRootLogin.*/PermitRootLogin\ yes/ /etc/ssh/sshd_config   
RUN sed -i s/#PasswordAuthentication\ /PasswordAuthentication\ / /etc/ssh/sshd_config
RUN echo "root:root" | chpasswd
RUN ssh-keygen -A
RUN adduser docker -D -s /bin/bash
RUN echo 'docker:docker' | chpasswd
RUN echo '%wheel ALL=(ALL) ALL' > /etc/sudoers.d/wheel
RUN addgroup docker wheel
RUN wget https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.109/bin/apache-tomcat-7.0.109.tar.gz
RUN tar xvzf apache-tomcat-7.0.109.tar.gz
RUN wget https://archive.apache.org/dist/archiva/2.2.3/binaries/apache-archiva-2.2.3.war
RUN echo -e "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tomcat-users xmlns=\"http://tomcat.apache.org/xml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://tomcat.apache.org/xml tomcat-users.xsd\"  version=\"1.0\">\n<role rolename=\"admin\"/>\n<role rolename=\"manager\"/>\n<role rolename=\"manager-gui\"/>\n<user username=\"admin\" password=\"12345678\" roles=\"admin,manager,manager-gui\"/>\n</tomcat-users>" > /apache-tomcat-7.0.109/conf/tomcat-users.xml
RUN echo -e "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Context antiResourceLocking=\"false\" privileged=\"true\">\n  <CookieProcessor className=\"org.apache.tomcat.util.http.Rfc6265CookieProcessor\" sameSiteCookies=\"strict\" />\n<Valve className=\"org.apache.catalina.valves.RemoteAddrValve\" allow=\".*\"/>\n<Manager sessionAttributeValueClassNameFilter=\"java\.lang\.(?:Boolean|Integer|Long|Number|String)|org\.apache\.catalina\.filters\.CsrfPreventionFilter\$LruCache(?:\$1)?|java\.util\.(?:Linked)?HashMap\"/></Context>" > /apache-tomcat-7.0.109/webapps/manager/META-INF/context.xml
RUN cp /apache-tomcat-7.0.109/webapps/manager/META-INF/context.xml /apache-tomcat-7.0.109/webapps/host-manager/META-INF/context.xml 
RUN cp /apache-tomcat-7.0.109/webapps/manager/META-INF/context.xml /apache-tomcat-7.0.109/webapps/host-manager/WEB-INF/manager.xml
RUN export CATALINA_OPTS="-Dappserver.home=$CATALINA_HOME -Dappserver.base=$CATALINA_HOME" 
RUN cd /apache-tomcat-7.0.109/lib && wget https://repo1.maven.org/maven2/org/apache/derby/derby/10.1.3.1/derby-10.1.3.1.jar
RUN cd /apache-tomcat-7.0.109/lib && wget https://repo1.maven.org/maven2/javax/mail/mail/1.4.1/mail-1.4.1.jar
RUN cd /apache-tomcat-7.0.109/lib && wget https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar
RUN mkdir /apache-tomcat-7.0.109/archiva/
RUN cp /apache-archiva-2.2.3.war /apache-tomcat-7.0.109/archiva/
RUN mkdir -p /apache-tomcat-7.0.109/conf/Catalina/localhost/
RUN echo '<Context path="/archiva" docBase="${catalina.home}/archiva/apache-archiva-2.2.3.war"> <Resource name="jdbc/users" auth="Container" type="javax.sql.DataSource" username="sa" password="" driverClassName="org.apache.derby.jdbc.EmbeddedDriver" url="jdbc:derby:/path/to/database/users;create=true" />  <Resource name="mail/Session" auth="Container" type="javax.mail.Session" mail.smtp.host="localhost"/> </Context>' > /apache-tomcat-7.0.109/conf/Catalina/localhost/archiva.xml 
RUN mkdir -p /root/.m2/
RUN touch /root/.m2/archiva.xml
RUN echo '#!/bin/sh' >> /entrypoint.sh
RUN echo 'export CATALINA_OPTS="-Dappserver.home=$CATALINA_HOME -Dappserver.base=$CATALINA_HOME"' >> /entrypoint.sh
RUN echo 'nohup /apache-tomcat-7.0.109/bin/catalina.sh start &' >> /entrypoint.sh
RUN echo 'nohup /usr/sbin/sshd -D -E /tmp/sshd.log &' >> /entrypoint.sh
RUN echo "cat" >> /entrypoint.sh
RUN chmod 755 /entrypoint.sh

EXPOSE 22 8082
CMD ["/entrypoint.sh"]
