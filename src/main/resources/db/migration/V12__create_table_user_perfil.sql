-- ===============================
-- Migration: criar tabelas Usuario e Perfil e relacionamentos
-- ===============================

-- 1️⃣ Tabela de Perfis
CREATE TABLE IF NOT EXISTS TB_PERFIL (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL
);

-- Populando perfis iniciais
INSERT INTO TB_PERFIL (nome) VALUES
('ADMIN'),
('USER'),
('MODERATOR');

-- 2️⃣ Tabela de Usuários
CREATE TABLE IF NOT EXISTS TB_USUARIOS (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- Populando usuários iniciais
INSERT INTO TB_USUARIOS (nome, email, senha) VALUES
('Wellerson', 'wellerson@email.com', 'senha123'),
('Maria', 'maria@email.com', 'senha123'),
('João', 'joao@email.com', 'senha123');

-- 3️⃣ Tabela de relacionamento Many-to-Many
CREATE TABLE IF NOT EXISTS usuario_perfis (
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIOS(id) ON DELETE CASCADE,
    CONSTRAINT fk_perfil FOREIGN KEY (perfil_id) REFERENCES TB_PERFIL(id) ON DELETE CASCADE
);

-- Populando relacionamentos iniciais
-- Wellerson = ADMIN + USER
INSERT INTO usuario_perfis (usuario_id, perfil_id) VALUES
(1, 1),  -- ADMIN
(1, 2),  -- USER
-- Maria = USER
(2, 2),
-- João = USER + MODERATOR
(3, 2),
(3, 3);