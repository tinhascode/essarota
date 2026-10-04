CREATE TABLE IF NOT EXISTS usuarios (
    id BINARY(16) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    telefone_whatsapp VARCHAR(20),
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS trajetos (
    id BINARY(16) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    origem VARCHAR(150) NOT NULL,
    destino VARCHAR(150) NOT NULL,
    tempo_estimado_min INT,
    CONSTRAINT pk_trajetos PRIMARY KEY (id),
    CONSTRAINT fk_trajetos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS linhas (
    id BINARY(16) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    CONSTRAINT pk_linhas PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS trajetos_linhas (
    id BINARY(16) NOT NULL,
    trajeto_id BINARY(16) NOT NULL,
    linha_id BINARY(16) NOT NULL,
    ordem INT NOT NULL,
    CONSTRAINT pk_trajetos_linhas PRIMARY KEY (id),
    CONSTRAINT uk_trajeto_linha UNIQUE (trajeto_id, linha_id),
    CONSTRAINT fk_tl_trajeto FOREIGN KEY (trajeto_id) REFERENCES trajetos (id) ON DELETE CASCADE,
    CONSTRAINT fk_tl_linha FOREIGN KEY (linha_id) REFERENCES linhas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS alertas (
    id BINARY(16) NOT NULL,
    linha_id BINARY(16) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    severidade VARCHAR(30) NOT NULL,
    criado_em TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_alertas PRIMARY KEY (id),
    CONSTRAINT fk_alertas_linha FOREIGN KEY (linha_id) REFERENCES linhas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notificacoes (
    id BINARY(16) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    alerta_id BINARY(16) NOT NULL,
    canal VARCHAR(30) NOT NULL,
    enviado_em TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_notificacoes PRIMARY KEY (id),
    CONSTRAINT fk_notificacoes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT fk_notificacoes_alerta FOREIGN KEY (alerta_id) REFERENCES alertas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
