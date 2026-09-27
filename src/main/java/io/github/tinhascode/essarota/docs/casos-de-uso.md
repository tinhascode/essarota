# Casos de Uso do Backend - Essarota

Este documento descreve e visualiza os Casos de Uso disponíveis no backend da aplicação **Essarota**, detalhando as capacidades e ações que o usuário (anônimo ou autenticado via JWT) e o sistema possuem na API REST.

---

## 1. Diagrama de Casos de Uso

```mermaid
flowchart LR
    subgraph Atores [Atores]
        Anonimo["Usuário Visitante (Sem Token)"]
        Autenticado["Usuário Autenticado (Com JWT)"]
        Sistema["Sistema / Monitor Operacional"]
    end

    subgraph ModuloAuth ["1. Autenticação e Conta"]
        UC01["Cadastrar Conta (Sign Up)"]
        UC02["Autenticar / Fazer Login (Gerar Token JWT)"]
        UC03["Visualizar Dados do Perfil"]
        UC04["Atualizar Dados do Perfil"]
        UC05["Excluir Conta"]
    end

    subgraph ModuloTrajetos ["2. Gestão de Trajetos"]
        UC06["Cadastrar Trajeto (Origem, Destino, Tempo Estimado)"]
        UC07["Calcular Tempo de Percurso Automaticamente"]
        UC08["Listar Meus Trajetos"]
        UC09["Buscar Trajeto por ID"]
        UC10["Atualizar Trajeto"]
        UC11["Excluir Trajeto"]
    end

    subgraph ModuloLinhasTrajetos ["3. Composição de Linhas no Trajeto"]
        UC12["Associar Linha ao Trajeto (Definir Ordem de Embarque)"]
        UC13["Listar Linhas de um Trajeto"]
        UC14["Remover Linha do Trajeto"]
    end

    subgraph ModuloLinhas ["4. Catálogo de Linhas de Transporte"]
        UC15["Consultar Todas as Linhas (Ônibus, Metrô, Trem)"]
        UC16["Buscar Detalhes de uma Linha por ID"]
        UC17["Cadastrar Nova Linha no Sistema"]
        UC18["Atualizar Informações da Linha"]
        UC19["Remover Linha"]
    end

    subgraph ModuloAlertas ["5. Alertas de Ocorrências"]
        UC20["Consultar Todos os Alertas Ativos"]
        UC21["Filtrar Alertas por Linha Específica"]
        UC22["Buscar Alerta por ID"]
        UC23["Registrar Alerta / Ocorrência na Linha"]
        UC24["Remover Alerta"]
    end

    subgraph ModuloNotificacoes ["6. Notificações do Usuário"]
        UC25["Consultar Histórico de Notificações Recebidas (/me)"]
        UC26["Buscar Notificação por ID"]
        UC27["Registrar Notificação Disparada (WhatsApp / Push)"]
    end

    %% Relacionamentos do Visitante
    Anonimo --> UC01
    Anonimo --> UC02

    %% Relacionamentos do Usuário Autenticado
    Autenticado --> UC03
    Autenticado --> UC04
    Autenticado --> UC05

    Autenticado --> UC06
    UC06 -.->|"include (se omitido)"| UC07
    Autenticado --> UC08
    Autenticado --> UC09
    Autenticado --> UC10
    Autenticado --> UC11

    Autenticado --> UC12
    Autenticado --> UC13
    Autenticado --> UC14

    Autenticado --> UC15
    Autenticado --> UC16
    Autenticado --> UC20
    Autenticado --> UC21
    Autenticado --> UC22

    Autenticado --> UC25
    Autenticado --> UC26

    %% Relacionamentos do Sistema / Administração / Monitor
    Sistema --> UC17
    Sistema --> UC18
    Sistema --> UC19
    Sistema --> UC23
    Sistema --> UC24
    Sistema --> UC27
```

---

## 2. Detalhamento das Ações do Usuário por Módulo

| Módulo | Ações Disponíveis para o Usuário | Endpoint REST |
| :--- | :--- | :--- |
| **Acesso & Conta** | • Criar uma nova conta com e-mail único e senha criptografada (BCrypt)<br>• Realizar login para obter o token JWT<br>• Visualizar seu cadastro, atualizar dados ou excluir sua conta | `POST /api/v1/usuarios`<br>`POST /api/v1/auth/login`<br>`GET, PUT, DELETE /api/v1/usuarios/{id}` |
| **Trajetos Pessoais** | • Cadastrar rotas rotineiras (ex: casa $\rightarrow$ trabalho)<br>• Deixar o tempo de trajeto ser calculado automaticamente se omitido<br>• Consultar apenas os seus próprios trajetos<br>• Atualizar ou excluir qualquer um de seus trajetos | `POST /api/v1/trajetos`<br>`GET /api/v1/trajetos`<br>`GET, PUT, DELETE /api/v1/trajetos/{id}` |
| **Trajeto $\leftrightarrow$ Linhas** | • Montar a sequência de conduções do trajeto (ex: 1º Ônibus linha X, 2º Metrô linha Y)<br>• Listar todas as linhas que compõem aquele trajeto específico<br>• Desassociar uma linha do percurso | `POST /api/v1/trajetos/{trajetoId}/linhas`<br>`GET /api/v1/trajetos/{trajetoId}/linhas`<br>`DELETE /api/v1/trajetos/{trajetoId}/linhas/{linhaId}` |
| **Consulta de Linhas** | • Navegar pelo catálogo de linhas de transporte cadastradas na rede<br>• Obter detalhes de tipo de modal (Metrô, Trem, Ônibus) de cada linha | `GET /api/v1/linhas`<br>`GET /api/v1/linhas/{id}` |
| **Acompanhamento de Alertas** | • Consultar ocorrências ativas no transporte público<br>• Filtrar alertas por uma linha de interesse para saber se há falha, lentidão ou paralisação | `GET /api/v1/alertas`<br>`GET /api/v1/alertas?linhaId={id}` |
| **Notificações Recebidas** | • Consultar sua caixa de histórico de notificações recebidas (WhatsApp ou Push)<br>• Inspecionar o detalhe de qual alerta disparou o aviso | `GET /api/v1/notificacoes/me`<br>`GET /api/v1/notificacoes/{id}` |
