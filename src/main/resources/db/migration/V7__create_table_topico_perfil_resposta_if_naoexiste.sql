-- ===============================
-- V7__ajustes_topico_perfil_respostas.sql
-- Migration segura para tabelas já existentes
-- ===============================

-- ===============================
-- 1️⃣ Adicionando coluna 'status' no TB_TOPICO se não existir
-- ===============================
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name='tb_topico'
          AND column_name='status'
    ) THEN
        ALTER TABLE tb_topico
        ADD COLUMN status VARCHAR(20) DEFAULT 'RASCUNHO';
    END IF;
END
$$;

-- ===============================
-- 2️⃣ Criando tabela TB_PERFIL se não existir
-- ===============================
CREATE TABLE IF NOT EXISTS tb_perfil (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL
);

-- ===============================
-- 3️⃣ Criando tabela TB_TOPICO se não existir
-- ===============================
CREATE TABLE IF NOT EXISTS tb_topico (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(250) NOT NULL,
    mensagem TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'RASCUNHO',
    curso_id BIGINT NOT NULL,
    CONSTRAINT fk_topico_curso
        FOREIGN KEY (curso_id) REFERENCES tb_cursos(id)
);

-- ===============================
-- 4️⃣ Criando tabela TB_RESPOSTAS se não existir
-- ===============================
CREATE TABLE IF NOT EXISTS tb_respostas (
    id BIGSERIAL PRIMARY KEY,
    mensagem VARCHAR(300),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    solucao BOOLEAN DEFAULT FALSE,
    topico_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    CONSTRAINT fk_resposta_topico
        FOREIGN KEY (topico_id) REFERENCES tb_topico(id),
    CONSTRAINT fk_resposta_curso
        FOREIGN KEY (curso_id) REFERENCES tb_cursos(id)
);