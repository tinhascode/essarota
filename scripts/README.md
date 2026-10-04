# Teste de Envio de Mensagens WhatsApp via Evolution API

Este diretório contém utilitários para validar e testar o envio de mensagens pelo WhatsApp utilizando a **Evolution API** integrada ao projeto **Essarota**.

---

## Pré-requisitos

1. **Containers em execução**:
   Certifique-se de que a Evolution API está rodando via Docker:
   ```bash
   docker compose up -d evolution-api
   ```
2. **Instância conectada ao WhatsApp**:
   A instância deve ter sido criada e autenticada escaneando o QR Code no seu WhatsApp.
3. **Variáveis de Ambiente**:
   Os scripts utilizam as credenciais definidas no arquivo `.env` na raiz do projeto:
   ```env
   SERVER_URL=http://localhost:8081
   AUTHENTICATION_API_KEY=sua_api_key_aqui
   ```

---

## 1. Script Python (`scripts/test_whatsapp.py`)

> **Vantagem:** Não requer instalação de pacotes externos (`pip`). Utiliza exclusivamente a biblioteca padrão do Python 3 (`urllib`, `json`, `argparse`).

### Permissão de Execução (se necessário)
```bash
chmod +x scripts/test_whatsapp.py
```

### Modo Interativo
O script detecta automaticamente instâncias ativas na Evolution API, solicita os dados no terminal e formata o telefone:
```bash
python3 scripts/test_whatsapp.py
```

### Modo via Linha de Comando (CLI)
Você pode passar todos os argumentos diretamente:
```bash
python3 scripts/test_whatsapp.py \
  -i "nome_da_instancia" \
  -n "11999998888" \
  -m "Olá! Mensagem de teste do Essarota 🚀"
```

### Parâmetros Suportados:
| Parâmetro | Flag Curta | Descrição | Padrão |
| :--- | :--- | :--- | :--- |
| `--instance` | `-i` | Nome da instância criada na Evolution API | Interativo / Auto-detectada |
| `--number` | `-n` | Telefone com DDD (ex: `11999998888` ou `5511999998888`) | Interativo |
| `--message` | `-m` | Texto da mensagem a ser enviada | Mensagem padrão de boas-vindas |
| `--url` | `-u` | URL base da Evolution API | Lê do `.env` ou `http://localhost:8081` |
| `--apikey` | `-k` | Chave de autenticação (`apikey`) | Lê do `.env` |

---

## 2. Script Shell / cURL (`scripts/test_whatsapp.sh`)

Para execuções rápidas diretamente pelo terminal Linux usando cURL.

### Permissão de Execução
```bash
chmod +x scripts/test_whatsapp.sh
```

### Sintaxe
```bash
./scripts/test_whatsapp.sh <NOME_DA_INSTANCIA> <NUMERO_COM_DDD> [MENSAGEM_OPCIONAL]
```

### Exemplo
```bash
./scripts/test_whatsapp.sh essarota_bot 11999998888 "Teste rápido via terminal"
```

---

## Resolução de Problemas Comuns

### 1. `Falha de conexão com a Evolution API` / `Connection refused`
- Verifique se o container da Evolution API está rodando:
  ```bash
  docker ps | grep evolution_api
  ```
- Teste o endpoint básico:
  ```bash
  curl -I http://localhost:8081
  ```

### 2. `HTTP 401 Unauthorized`
- A chave `AUTHENTICATION_API_KEY` fornecida difere da configurada no arquivo `.env` do container.
- Verifique a chave configurada em `.env` e reinicie os containers se tiver alterado:
  ```bash
  docker compose up -d evolution-api
  ```

### 3. `HTTP 400` ou `Instance not connected`
- A instância informada pode não estar conectada ao WhatsApp.
- Reabra a interface ou faça a chamada de status para checar se o estado é `open`:
  ```bash
  curl -s -H "apikey: SUA_KEY" http://localhost:8081/instance/connectionState/NOME_DA_INSTANCIA
  ```
