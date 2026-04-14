-- ===============================
-- Migration V13: Ajustada
-- ===============================

-- 1. Garante que as tabelas base existam e tenham dados
-- (Sua parte de TB_USUARIOS e TB_PERFIL está ótima)

-- 2. Ajuste na TB_TOPICO
ALTER TABLE TB_TOPICO ADD COLUMN IF NOT EXISTS usuario_id BIGINT;
-- Se você tiver uma tabela TB_CURSO, certifique-se de que o curso_id 1 existe aqui
ALTER TABLE TB_TOPICO ADD COLUMN IF NOT EXISTS curso_id BIGINT;

-- Popular dados existentes para não quebrar o NOT NULL
UPDATE TB_TOPICO SET usuario_id = 1 WHERE usuario_id IS NULL;

-- Aplicar restrições
ALTER TABLE TB_TOPICO ALTER COLUMN usuario_id SET NOT NULL;

-- 3. Ajuste na TB_RESPOSTAS
ALTER TABLE TB_RESPOSTAS ADD COLUMN IF NOT EXISTS usuario_id BIGINT;

-- O campo na entidade é 'autor', mas no banco é 'usuario_id'. Correto!
UPDATE TB_RESPOSTAS SET usuario_id = 1 WHERE usuario_id IS NULL;
ALTER TABLE TB_RESPOSTAS ALTER COLUMN usuario_id SET NOT NULL;

-- 4. Chaves Estrangeiras (FKs)
-- Dica: Use nomes únicos para as constraints para evitar conflitos
ALTER TABLE TB_TOPICO DROP CONSTRAINT IF EXISTS fk_topico_usuario;
ALTER TABLE TB_TOPICO ADD CONSTRAINT fk_topico_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIOS(id);

ALTER TABLE TB_RESPOSTAS DROP CONSTRAINT IF EXISTS fk_resposta_usuario;
ALTER TABLE TB_RESPOSTAS ADD CONSTRAINT fk_resposta_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIOS(id);

-- 5. Inserts de Teste (Ajustados com Enums em Caps e IDs de Relacionamento)
INSERT INTO TB_TOPICO (titulo, mensagem, status, usuario_id, curso_id, data_criacao)
VALUES ('Como configurar Spring Boot?', 'Ajuda com config', 'Publicado', 1, 1, NOW());