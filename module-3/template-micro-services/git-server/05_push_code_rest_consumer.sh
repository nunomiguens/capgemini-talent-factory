#1/bin/sh
cp ../../template-rest-api-persistance-messaging/rest-server -R ~/tmp/hello/ 
cp gitignore.sample ~/tmp/hello/.gitignore
cd ~/tmp/hello/
git add .gitignore rest-server/
git commit -m "added rest-server"
git pull origin master
git push origin master 
git log 
