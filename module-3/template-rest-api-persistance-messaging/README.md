# Steps 
REST API, persistance and messaging template steps. 

```sh
# disable k3s
sudo systemctl stop k3s.service
sudo systemctl disable k3s.service

# disable jenkins 
sudo systemctl stop jenkins
sudo systemctl disable jenkins

# run example
sh 01_setup.sh
cd rest-server 
sh run.sh
cd ..
```

# Testes

```
# Criar Restaurantes
curl -X POST -H 'Content-Type: application/json' localhost:8080/criaRestaurante --data '{"nome":"Taska da Estação", "morada":"Rua em Cima Debaixo", "codigoPostal":"5500-221"}'
curl -X POST -H 'Content-Type: application/json' localhost:8080/criaRestaurante --data '{"nome":"Nham Nham", "morada":"Rua do Outro Lado", "codigoPostal":"7350-666"}'
curl -X POST -H 'Content-Type: application/json' localhost:8080/criaRestaurante --data '{"nome":"Oligarca", "morada":"Av do Castelo Preto", "codigoPostal":"1978-123"}'
```

```
# Criar Menus
curl -X POST -H 'Content-Type: application/json' localhost:8080/criaMenu/Nham%20Nham --data '{"nome":"Menu Segunda","descricao":"As descrições devem ser calorosas e transmitir a alma do restaurante. Elas precisam cativar o cliente e ...","opcaoMenu":[{"carne":"Bife Tártaro", "peixe":"Bacalhau Polo Norte"}]}'
curl -X POST -H 'Content-Type: application/json' localhost:8080/criaMenu/Oligarca --data '{"nome":"Menu Quarta","descricao":"As descrições devem ser calorosas e transmitir a alma do restaurante. Elas precisam cativar o cliente e ...","opcaoMenu":[{"carne":"Frango Esturricado", "peixe":"Salmão Branco"}]}'
```

```
# Ver Restaurantes
curl localhost:8080/listaRestaurantes
```

```
# Ver Menus
curl localhost:8080/listaMenus/Nham%20Nham
curl localhost:8080/listaMenus/Oligarca
```

```
# Criar Encomendas
curl -X POST -H 'Content-Type: application/json' localhost:8080/criaEncomenda/Oligarca --data '["Menu Quarta"]'
curl -X POST -H 'Content-Type: application/json' localhost:8080/criaEncomenda/Nham%20Nham --data '["Menu Segunda"]'
```

```
# Ver Encomenda
curl localhost:8080/listarEncomenda/1
curl localhost:8080/listarEncomenda/2
```

```
# Processa Encomenda
curl -X POST localhost:8080/processaEncomenda/1
curl -X POST localhost:8080/processaEncomenda/2
```

```
# Ver Histórico Encomendas
curl localhost:8080/historicoEncomenda
```

# Cleanup 
```sh
docker stop consumer-container rest-server-container activemq-server-container \
  kafka-server zookeeper-server postgres-server
```
