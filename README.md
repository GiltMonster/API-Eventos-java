# 📅 API de Eventos com Quarkus

Essa é uma API REST desenvolvida com **Quarkus** para gerenciamento de **eventos**, **inscrições de usuários** e **eventos favoritos** caso deseje ter um acesso rápido ao conteúdo dessa API:

## 📥 Acesso Rápido

| Ação | Link | Descrição |
|--------|-----------|-----------|
| Download Collection Postman | [Baixar Collection do Postman](./src/main/resources/swagger/APIdeEventos.postman_collection.json) | Collection do Postman para importar e testar a API |
| Run in Postman | [![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/32743440-b847d552-7038-42a1-88bf-bc1eb288601a?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D32743440-b847d552-7038-42a1-88bf-bc1eb288601a%26entityType%3Dcollection%26workspaceId%3Dd987db3b-aaaa-438e-af39-4a60109981f5) | Execute a coleção diretamente no Postman |
| Acesse via Railway | [![Deploy on Railway](https://railway.app/button.svg)](https://api-eventos-tsi-production.up.railway.app/swagger.html) | Veja a API rodando no Railway |

## 🧑‍💻 Autor

- **Lucas S. Campos**
- **Data:** 06/04/2025
- **Versão:** 2.0.0
- **Repositório:** [API Eventos TSI](https://github.com/GiltMonster/API-Eventos-java)
- **Latest Version deployed:** [Latest Version](https://api-eventos-tsi-production.up.railway.app/documentacao/)

---

## 🚀 Tecnologias Utilizadas

- Java + Quarkus
- JPA / Hibernate
- Banco de Dados (H2, PostgreSQL, etc)
- Swagger (documentação via OpenAPI)
- BCrypt (criptografia de senhas)
- Autenticação via API Key (v2)
- CORS habilitado (v2)

---

## 📚 Documentação das Versões

- **v1 (sem autenticação):**  
  [Swagger v1](https://api-eventos-tsi-production.up.railway.app/swagger.html?version=v1)
- **v2 (com autenticação por API Key):**  
  [Swagger v2](https://api-eventos-tsi-production.up.railway.app/swagger.html?version=v2)

---

## 🆚 Diferenças entre v1 e v2

| Característica         | v1 (legacy)         | v2 (recomendada)         |
|------------------------|---------------------|--------------------------|
| Autenticação           | Não                 | Sim (API Key via header) |
| CORS                   | Não                 | Sim                      |
| Endpoints Favoritos    | Não                 | Sim                      |
| Segurança              | Baixa               | Alta                     |
| Recomendada para uso?  | Não                 | Sim                      |

> **Atenção:** A v1 será descontinuada em breve. Use a v2 para novos projetos.

---

## 🔐 Autenticação na v2

Todos os endpoints da v2 (exceto `/v2/apikeys/generate/{userId}`) exigem o envio do header:

``` bash
X-API-KEY: sua-chave-gerada
```

- Gere sua chave via endpoint `/v2/apikeys/generate/{userId}`.
- Consulte suas chaves em `/v2/apikeys/user/{userId}`.

Exemplo de requisição autenticada:

``` bash
curl -H "X-API-KEY: sua-chave" https://api-eventos-tsi-production.up.railway.app/v2/eventos
```

---

## 🛠️ Como rodar localmente

1. Clone o repositório:

```bash
git clone https://github.com/GiltMonster/API-Eventos-TSI
cd API-Eventos-TSI
```

2. Execute o projeto com:

``` bash
./mvnw quarkus:dev
```

> A API será disponibilizada em: `http://localhost:8080/documentacao/`

---

## 📦 Endpoints Principais

### 🎟️ Evento Resource

| Método | Rota (v2) | Descrição |
|--------|-----------|-----------|
| `GET`  | `/v2/eventos` | Listar todos os eventos |
| `POST` | `/v2/eventos` | Cadastrar novo evento *(header Idempotency-Key obrigatório)* |
| `PUT`  | `/v2/eventos` | Atualizar evento existente |
| `DELETE` | `/v2/eventos/deletarEvento/{id}` | Deletar evento |
| `GET`  | `/v2/eventos/findEvento/{id}` | Buscar evento por ID |

### ⭐ Evento Favorito Resource (v2)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/v2/eventosFav/addEventoFavorito/{userId}/{eventoId}` | Adicionar evento aos favoritos *(header Idempotency-Key obrigatório)* |
| `DELETE` | `/v2/eventosFav/deleteEventoFavorito/{id}` | Remover evento favorito |
| `GET` | `/v2/eventosFav/getAllEventosFavoritos/{userId}` | Listar todos os favoritos do usuário |
| `GET` | `/v2/eventosFav/verificarFavorito/{userId}/{eventoId}` | Verificar se evento é favorito |

### 🙋‍♂️ Inscricao Resource

| Método | Rota (v2) | Descrição |
|--------|-----------|-----------|
| `POST` | `/v2/inscricoes/realizaInscricao/{userId}/{eventoId}` | Inscrever usuário em evento *(header Idempotency-Key obrigatório)* |
| `GET`  | `/v2/inscricoes/inscricoesUsuario/estaInscrito/{userId}/{eventoId}` | Verifica se usuário está inscrito |
| `GET`  | `/v2/inscricoes/inscricoesUsuario/{userId}` | Lista todas as inscrições de um usuário |
| `PUT`  | `/v2/inscricoes/inscricoesUsuario` | Atualiza inscrição de um usuário |
| `DELETE` | `/v2/inscricoes/inscricoesUsuario/{userId}/{eventoId}` | Remove a inscrição de um usuário |

### 👤 Usuario Resource

| Método | Rota (v2) | Descrição |
|--------|-----------|-----------|
| `GET`  | `/v2/usuarios` | Listar todos os usuários |
| `POST` | `/v2/usuarios` | Cadastrar novo usuário *(header Idempotency-Key obrigatório)* |
| `PUT`  | `/v2/usuarios/atualizarUsuario/{id}` | Atualizar dados do usuário |
| `DELETE` | `/v2/usuarios/deleteUsuario/{id}` | Deletar usuário |
| `GET`  | `/v2/usuarios/findUsuario/{id}` | Buscar usuário por ID |

### 🔑 API Key Resource (v2)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/v2/apikeys/generate/{userId}` | Gerar nova chave de API |
| `GET`  | `/v2/apikeys/user/{userId}` | Listar chaves de API do usuário |
| `DELETE` | `/v2/apikeys/{id}` | Excluir chave de API |

### 🔓 Auth Resource (v2)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/v2/auth/login` | Login de usuário (retorna dados do usuário autenticado) |
| `PUT`  | `/v2/auth/atualizarSenha/{id}` | Atualizar senha do usuário |

---

## 📝 Exemplos de Uso

### Cadastro de Usuário (v2)

```bash
curl -X POST https://api-eventos-tsi-production.up.railway.app/v2/usuarios \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: sua-chave" \
  -H "Idempotency-Key: qualquer-string-unica" \
  -d '{"nome":"João","sobreNome":"Silva","email":"joao@gmail.com","senha":"senha123"}'
```

### Login (v2)

```bash
curl -X POST https://api-eventos-tsi-production.up.railway.app/v2/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@gmail.com","senha":"senha123"}'
```

### Gerar API Key

```bash
curl -X POST https://api-eventos-tsi-production.up.railway.app/v2/apikeys/generate/1?accessLevel=USER \
  -H "X-API-KEY: sua-chave"
```

---

## ☁️ Deploy no Railway

Você pode deployar esta API no [Railway](https://railway.app/) rapidamente!

### ✅ Pré-requisitos

- Conta Railway
- Projeto no GitHub

### 🚀 Passos para deploy

1. Suba seu projeto no GitHub

2. No Railway, clique em **New Project** > **Deploy from GitHub Repo**

3. Configure:

- **Build command:**

``` bash
  ./mvnw clean package -DskipTests
```

- **Start command:**

``` bash
  java -jar target/quarkus-app/quarkus-run.jar
```

4. Verifique se no `application.properties` você tem:

```properties
quarkus.http.host=0.0.0.0
quarkus.http.port=${PORT:8080}
```

5. Railway irá gerar uma URL pública para sua API. Exemplo:

```
https://api-eventos.up.railway.app/q/openapi
```

---

## 📄 Licença && 📛 Badges

![Quarkus](https://img.shields.io/badge/Quarkus-FFCA28?style=flat&logo=quarkus&logoColor=black)
![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
