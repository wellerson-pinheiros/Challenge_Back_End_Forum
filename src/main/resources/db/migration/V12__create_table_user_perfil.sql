-- 1️⃣ Removido o CREATE de TB_PERFIL porque já existe na V7

-- Populando perfis iniciais
INSERT INTO TB_PERFIL (nome) VALUES ('ADMIN'), ('USER'), ('MODERATOR')
ON CONFLICT (nome) DO NOTHING;

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
('João', 'joao@email.com', 'senha123')
ON CONFLICT (email) DO NOTHING;

-- 3️⃣ Tabela de relacionamento Many-to-Many
CREATE TABLE IF NOT EXISTS usuario_perfis (
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIOS(id) ON DELETE CASCADE,
    CONSTRAINT fk_perfil FOREIGN KEY (perfil_id) REFERENCES TB_PERFIL(id) ON DELETE CASCADE
);

-- Populando relacionamentos
INSERT INTO usuario_perfis (usuario_id, perfil_id) VALUES
(1, (SELECT id FROM TB_PERFIL WHERE nome = 'ADMIN')),
(1, (SELECT id FROM TB_PERFIL WHERE nome = 'USER')),
(2, (SELECT id FROM TB_PERFIL WHERE nome = 'USER'))
ON CONFLICT DO NOTHING;