-- Postgresql initial state
CREATE TABLE pagamentoFaturas (
  numeroFatura VARCHAR (255)NOT NULL,
  descricaoFatura VARCHAR (255)NOT NULL,
  estadoFatura VARCHAR (255) NOT NULL 
);

insert into pagamentoFaturas (numeroFatura,descricaoFatura,estadoFatura) values('O1','Lorem Ipsum is simply dummy text of the','');
insert into pagamentoFaturas (numeroFatura,descricaoFatura,estadoFatura) values('O2','Lorem Ipsum is simply dummy text of the','Registada');
insert into pagamentoFaturas (numeroFatura,descricaoFatura,estadoFatura) values('O3','Lorem Ipsum is simply dummy text of the','Registada');
insert into pagamentoFaturas (numeroFatura,descricaoFatura,estadoFatura) values('O4','Lorem Ipsum is simply dummy text of the','');
insert into pagamentoFaturas (numeroFatura,descricaoFatura,estadoFatura) values('O5','Lorem Ipsum is simply dummy text of the','Registada');

