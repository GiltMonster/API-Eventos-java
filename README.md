# 📅 API de Eventos com Quarkus

Essa é uma API REST desenvolvida com **Quarkus** para gerenciamento de **eventos** e **inscrições de usuários**.

## 🧑‍💻 Autor

- **Lucas S. Campos**
- **Data:** 06/04/2025
- **Versão:** 1.0.0
- **Repositório:** [API Eventos TSI](https://github.com/seu-usuario/seu-repo)
- **Latest Version deployed:** [Latest Version](https://api-eventos-tsi-production.up.railway.app/documentacao/)

---

## 🚀 Tecnologias Utilizadas

- Java + Quarkus
- JPA / Hibernate
- Banco de Dados (H2, PostgreSQL, etc)
- Swagger (documentação via OpenAPI)
- BCrypt (criptografia de senhas)

---

## 🛠️ Como rodar localmente

1. Clone o repositório:

```bash
git clone https://github.com/GiltMonster/API-Eventos-TSI
cd seu-repo
```

2. Execute o projeto com:

```bash
./mvnw quarkus:dev
```

> A API será disponibilizada em: `[Latest Version](https://api-eventos-tsi-production.up.railway.app/documentacao/)`

---

## 📚 Documentação Swagger

Acesse via:

```
[Latest Version](https://api-eventos-tsi-production.up.railway.app/documentacao/)
```

---

## 🔐 Cadastro de Usuários

Usuários são cadastrados com os campos:

```sql
insert into usuario (nome, sobreNome, email, senha) values('Lucas', 'Santos', 'lucas@gmail.com', '123456');
```

> ⚠️ As senhas cadastradas diretamente no banco **não estão criptografadas**. Ao usar login ou trocar a senha via API, a criptografia com BCrypt é aplicada.

---

## 📦 Endpoints Principais

### 🎟️ Evento Resource

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET`  | `/eventos` | Listar todos os eventos |
| `POST` | `/eventos` | Cadastrar novo evento |
| `PUT`  | `/eventos` | Atualizar evento existente |
| `DELETE` | `/eventos/deletarEvento/{id}` | Deletar evento |
| `GET`  | `/eventos/findEvento/{id}` | Buscar evento por ID |

---

### 🙋‍♂️ Inscricao Resource

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/inscricoes/realizaInscricao/{userId}/{eventoId}` | Inscrever usuário em evento |
| `GET`  | `/inscricoes/inscricoesUsuario/estaInscrito/{userId}/{eventoId}` | Verifica se usuário está inscrito |
| `GET`  | `/inscricoes/inscricoesUsuario/{userId}` | Lista todas as inscrições de um usuário |
| `PUT`  | `/inscricoes/inscricoesUsuario` | Atualiza inscrição de um usuário |
| `DELETE` | `/inscricoes/inscricoesUsuario/{userId}/{eventoId}` | Remove a inscrição de um usuário |

---

### 👤 Usuario Resource

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET`  | `/usuarios` | Listar todos os usuários |
| `POST` | `/usuarios` | Cadastrar novo usuário |
| `PUT`  | `/usuarios/atualizarSenha/{id}` | Atualizar senha do usuário |
| `PUT`  | `/usuarios/atualizarUsuario/{id}` | Atualizar dados do usuário |
| `DELETE` | `/usuarios/deleteUsuario/{id}` | Deletar usuário |
| `GET`  | `/usuarios/findUsuario/{id}` | Buscar usuário por ID |
| `POST` | `/usuarios/login` | Login de usuário |

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
  ```bash
  ./mvnw clean package -DskipTests
  ```

- **Start command:**
  ```bash
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

## 📄 Licença

Apache 2.0
