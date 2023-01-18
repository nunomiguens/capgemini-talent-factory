#!/bin/sh
rm ~/.ssh/known_hosts 
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