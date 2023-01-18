#!/bin/sh
ssh-keygen
docker cp ~/.ssh/id_rsa.pub git-server-container:/tmp
docker exec -it git-server-container cat /tmp/id_rsa.pub
docker exec -it git-server-container sh -c 'cat /tmp/id_rsa.pub >> /home/git/.ssh/authorized_keys'
docker exec -it git-server-container sh -c 'cat /home/git/.ssh/authorized_keys'
