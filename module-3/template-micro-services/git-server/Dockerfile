FROM alpine:3.16
RUN apk add --update --no-cache curl wget nano vim openssh git sudo bash
RUN sed -i s/#PermitRootLogin.*/PermitRootLogin\ yes/ /etc/ssh/sshd_config   
RUN sed -i s/#PasswordAuthentication\ /PasswordAuthentication\ / /etc/ssh/sshd_config
RUN echo "root:root" | chpasswd
RUN ssh-keygen -A

RUN adduser docker -D -s /bin/bash
RUN echo 'docker:docker' | chpasswd
RUN adduser git -D -s /usr/bin/git-shell git
RUN echo 'git:git' | chpasswd
RUN echo '%wheel ALL=(ALL) ALL' > /etc/sudoers.d/wheel
RUN addgroup docker wheel
RUN cd /home/git && chown git . -R && chgrp git . -R 
RUN mkdir -p /home/git/.ssh
RUN touch /home/git/.ssh/authorized_keys
RUN chmod 700 /home/git/.ssh/
RUN chmod 600 /home/git/.ssh/authorized_keys
RUN mkdir -p /home/git/hello.git
RUN cd /home/git/hello.git && git --bare init
RUN cd /home/git && chown git . -R && chgrp git . -R  

EXPOSE 22
CMD ["/usr/sbin/sshd","-D","-E","/tmp/sshd.log"]
