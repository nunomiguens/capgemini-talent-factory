FROM alpine:3.16
RUN apk add --update --no-cache curl wget nano vim openssh sudo bash  openjdk17-jdk
RUN wget "https://www.apache.org/dyn/closer.cgi?filename=/activemq/5.17.1/apache-activemq-5.17.1-bin.tar.gz&action=download" -O apache-activemq-5.17.1-bin.tar.gz
RUN tar xvzf apache-activemq-5.17.1-bin.tar.gz 
RUN sed -i 's/127\.0\.0\.1/0\.0\.0\.0/g' /apache-activemq-5.17.1/conf/jetty.xml

EXPOSE 8161 61616 5672 61613 1883 61614
CMD ["/apache-activemq-5.17.1/bin/activemq","console"] 
