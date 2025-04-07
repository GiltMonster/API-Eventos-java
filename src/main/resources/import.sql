-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Usuários:
insert into usuario (nome, sobreNome, email, senha) values('Lucas', 'Santos', 'lucas@gmail.com', '123456');
insert into usuario (nome, sobreNome, email, senha) values('Ana', 'Silva', 'ana.silva@exemplo.com', 'senha123');
insert into usuario (nome, sobreNome, email, senha) values('Pedro', 'Oliveira', 'pedro.oliveira@exemplo.com', 'outrasenha');
insert into usuario (nome, sobreNome, email, senha) values('Maria', 'Souza', 'maria.souza@exemplo.com', 'senha456' );
insert into usuario (nome, sobreNome, email, senha) values('João', 'Pereira', 'joao.pereira@exemplo.com', 'senhadificil');
insert into usuario (nome, sobreNome, email, senha) values('Juliana', 'Gomes', 'juliana.gomes@exemplo.com', 'minhasenha');
insert into usuario (nome, sobreNome, email, senha) values('Rafael', 'Costa', 'rafael.costa@exemplo.com', 'senha789');
insert into usuario (nome, sobreNome, email, senha) values('Fernanda', 'Rodrigues', 'fernanda.rodrigues@exemplo.com', 'senhaforte');
insert into usuario (nome, sobreNome, email, senha) values('Gustavo', 'Almeida', 'gustavo.almeida@exemplo.com', 'senha1010');
insert into usuario (nome, sobreNome, email, senha) values('Camila', 'Machado', 'camila.machado@exemplo.com', 'senha1111');
insert into usuario (nome, sobreNome, email, senha) values('Bruno', 'Ribeiro', 'bruno.ribeiro@exemplo.com', 'senha1212');

--Eventos:
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Grande Show', 'Show de uma banda famosa.', '2023-11-15', '2023-11-15', 'Estádio Municipal');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Festa de Aniversário', 'Festa de aniversário surpresa para um amigo.', '2023-10-01', '2023-10-01', 'Rua das Flores, 123');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Gameshow', 'Evento de jogos eletrônicos.', '2023-12-05', '2023-12-05', 'Centro de Convenções');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Feira de Tecnologia', 'Exposição de inovações tecnológicas.', '2023-11-20', '2023-11-22', 'Centro de Exposições');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Festival de Música', 'Festival com várias bandas e artistas.', '2023-10-10', '2023-10-12', 'Parque da Cidade');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Conferência de Negócios', 'Encontro de empresários e investidores.', '2023-11-30', '2023-12-01', 'Hotel Luxo');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Workshop de Programação', 'Curso intensivo de programação.', '2023-10-20', '2023-10-22', 'Auditório da Universidade');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Exposição de Arte', 'Mostra de artistas locais.', '2023-11-05', '2023-11-10', 'Galeria de Arte');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Corrida de Rua', 'Competição de corrida.', '2023-12-10', '2023-12-10', 'Praça Central');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Feira de Alimentos', 'Exposição de produtos alimentícios.', '2023-11-15', '2023-11-17', 'Parque Municipal');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Encontro de Carros Antigos', 'Exposição de carros clássicos.', '2023-10-25', '2023-10-26', 'Praça do Pôr do Sol');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Festival de Cinema', 'Exibição de filmes independentes.', '2023-11-01', '2023-11-03', 'Teatro Municipal');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Feira de Artesanato', 'Exposição de produtos artesanais.', '2023-12-01', '2023-12-03', 'Praça da Liberdade');
insert into evento (nome, descricao, dataInicio, dataFim, localizacao) values('Encontro de Tecnologia', 'Discussão sobre inovações tecnológicas.', '2023-10-15', '2023-10-16', 'Centro de Inovação');