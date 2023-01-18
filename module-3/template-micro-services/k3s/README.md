# Install
```
sudo curl -sfL https://get.k3s.io | sh -
sudo groupadd k3s
sudo usermod -a vagrant -G k3s
sudo chown root:k3s /etc/rancher/k3s/k3s.yaml
sudo chmod 740 /etc/rancher/k3s/k3s.yaml
sudo apt update -y
sudo apt upgrade -y
```
