# Guia de Requisições da API para Postman / Insomnia

Guia com todas as requisições da API, URLs, cabeçalhos, corpos em formato JSON e respostas esperadas.

---

## 1. Configuração de Variáveis no Postman

Para facilitar os testes, crie as seguintes variáveis de ambiente ou da coleção no Postman:

| Variável | Valor Inicial Sugerido | Descrição |
| :--- | :--- | :--- |
| `baseUrl` | `http://localhost:8080` | URL base da aplicação Spring Boot |
| `token` | *(vazio - preenchido após login)* | Token JWT retornado no login |
| `usuarioId` | *(vazio - preenchido após criar/listar)* | UUID do usuário para busca, update e delete |

---

## 2. Autenticação & Usuários

### 2.1 Criar Usuário (Registro Público)
- **Método:** `POST`
- **URL:** `{{baseUrl}}/api/v1/usuarios`
- **Autenticação:** Nenhuma (Rota pública)
- **Headers:**
  - `Content-Type: application/json`

#### Body (JSON):
```json
{
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "senha": "senhaSegura123",
  "telefoneWhatsapp": "+5511999998888",
  "deviceToken": "fcm_token_device_abc123"
}
```

#### Resposta de Sucesso (`201 Created`):
```json
{
  "id": "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185",
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "telefoneWhatsapp": "+5511999998888",
  "deviceToken": "fcm_token_device_abc123"
}
```

> **Dica Postman (Tests):**  
> Para salvar automaticamente o `usuarioId` após criar:
> ```javascript
> if (pm.response.code === 201) {
>     const data = pm.response.json();
>     pm.environment.set("usuarioId", data.id);
> }
> ```

---

### 2.2 Fazer Login (Obter Token JWT)
- **Método:** `POST`
- **URL:** `{{baseUrl}}/api/v1/auth/login`
- **Autenticação:** Nenhuma (Rota pública)
- **Headers:**
  - `Content-Type: application/json`

#### Body (JSON):
```json
{
  "email": "joao.silva@email.com",
  "senha": "senhaSegura123"
}
```

#### Resposta de Sucesso (`200 OK`):
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvLnNpbHZhQGVtYWlsLmNvbSIsInVzdWFyaW9JZCI6ImM3YThiODRkLTJhM2ItNDFmNi1iNzg4LWI3M2VhNmZmOTE4NSIsIm5vbWUiOiJKb8OjbyBkYSBTaWx2YSIsImlhdCI6MTczNjgwMDAwMCwiZXhwIjoxNzM2ODg2NDAwfQ.assina...",
  "tipo": "Bearer",
  "usuarioId": "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185",
  "nome": "João da Silva",
  "email": "joao.silva@email.com"
}
```

> **Dica Postman (Tests):**  
> Para salvar o token automaticamente para as próximas requisições:
> ```javascript
> if (pm.response.code === 200) {
>     const data = pm.response.json();
>     pm.environment.set("token", data.token);
>     pm.environment.set("usuarioId", data.usuarioId);
> }
> ```

---

### 2.3 Listar Todos os Usuários
- **Método:** `GET`
- **URL:** `{{baseUrl}}/api/v1/usuarios`
- **Autenticação:** `Bearer Token` (`{{token}}`)
- **Headers:**
  - `Authorization: Bearer {{token}}`
- **Body:** Nenhum (`none`)

#### Resposta de Sucesso (`200 OK`):
```json
[
  {
    "id": "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185",
    "nome": "João da Silva",
    "email": "joao.silva@email.com",
    "telefoneWhatsapp": "+5511999998888",
    "deviceToken": "fcm_token_device_abc123"
  }
]
```

---

### 2.4 Buscar Usuário por ID
- **Método:** `GET`
- **URL:** `{{baseUrl}}/api/v1/usuarios/{{usuarioId}}`  
  *(Ex: `http://localhost:8080/api/v1/usuarios/c7a8b84d-2a3b-41f6-b788-b73ea6ff9185`)*
- **Autenticação:** `Bearer Token` (`{{token}}`)
- **Headers:**
  - `Authorization: Bearer {{token}}`
- **Body:** Nenhum (`none`)

#### Resposta de Sucesso (`200 OK`):
```json
{
  "id": "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185",
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "telefoneWhatsapp": "+5511999998888",
  "deviceToken": "fcm_token_device_abc123"
}
```

---

### 2.5 Atualizar Usuário
- **Método:** `PUT`
- **URL:** `{{baseUrl}}/api/v1/usuarios/{{usuarioId}}`  
  *(Ex: `http://localhost:8080/api/v1/usuarios/c7a8b84d-2a3b-41f6-b788-b73ea6ff9185`)*
- **Autenticação:** `Bearer Token` (`{{token}}`)
- **Headers:**
  - `Authorization: Bearer {{token}}`
  - `Content-Type: application/json`

#### Body (JSON):
> *Nota: O campo `senha` é opcional no update. Se enviado, atualizará a senha.*
```json
{
  "nome": "João da Silva Atualizado",
  "email": "joao.silva@email.com",
  "senha": "novaSenhaSegura456",
  "telefoneWhatsapp": "+5511988887777",
  "deviceToken": "fcm_token_device_novo456"
}
```

#### Resposta de Sucesso (`200 OK`):
```json
{
  "id": "c7a8b84d-2a3b-41f6-b788-b73ea6ff9185",
  "nome": "João da Silva Atualizado",
  "email": "joao.silva@email.com",
  "telefoneWhatsapp": "+5511988887777",
  "deviceToken": "fcm_token_device_novo456"
}
```

---

### 2.6 Deletar Usuário
- **Método:** `DELETE`
- **URL:** `{{baseUrl}}/api/v1/usuarios/{{usuarioId}}`  
  *(Ex: `http://localhost:8080/api/v1/usuarios/c7a8b84d-2a3b-41f6-b788-b73ea6ff9185`)*
- **Autenticação:** `Bearer Token` (`{{token}}`)
- **Headers:**
  - `Authorization: Bearer {{token}}`
- **Body:** Nenhum (`none`)

#### Resposta de Sucesso (`204 No Content`):
- Corpo vazio.

---

## 3. Exemplos de Erros Tratados

### 3.1 Falha de Autenticação / Token Ausente (`401 Unauthorized`)
Quando tentar acessar uma rota protegida sem o header `Authorization: Bearer <token>`:
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Acesso negado. Token JWT ausente ou inválido.",
  "path": "/api/v1/usuarios"
}
```

### 3.2 Credenciais Inválidas no Login (`401 Unauthorized`)
```json
{
  "timestamp": "2026-09-13T23:15:30.123456Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "E-mail ou senha inválidos.",
  "path": "/api/v1/auth/login",
  "fieldErrors": null
}
```

### 3.3 E-mail já Cadastrado (`409 Conflict`)
```json
{
  "timestamp": "2026-09-13T23:16:00.123456Z",
  "status": 409,
  "error": "Conflict",
  "message": "Já existe um usuário cadastrado com o e-mail: joao.silva@email.com",
  "path": "/api/v1/usuarios",
  "fieldErrors": null
}
```

### 3.4 Erro de Validação de Campos (`400 Bad Request`)
```json
{
  "timestamp": "2026-09-13T23:17:15.987654Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Dados de entrada inválidos",
  "path": "/api/v1/usuarios",
  "fieldErrors": {
    "nome": "O nome é obrigatório",
    "email": "Formato de e-mail inválido",
    "senha": "A senha deve ter no mínimo 6 caracteres"
  }
}
```

### 3.5 Usuário Não Encontrado (`404 Not Found`)
```json
{
  "timestamp": "2026-09-13T23:18:00.000000Z",
  "status": 404,
  "error": "Not Found",
  "message": "Usuário não encontrado com o ID: c7a8b84d-2a3b-41f6-b788-b73ea6ff9185",
  "path": "/api/v1/usuarios/c7a8b84d-2a3b-41f6-b788-b73ea6ff9185",
  "fieldErrors": null
}
```
