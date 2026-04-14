-- Inserindo o usuário que servirá de "âncora" para posts de contas deletadas
INSERT INTO TB_USUARIOS (id, nome, email, senha)
VALUES (100, 'Usuário Anônimo', 'anonimo@forumhub.com', 'senha123');

-- Garantindo que ele tenha um perfil básico de USER para não quebrar regras de negócio
-- Ajuste o nome da tabela de relacionamento se for diferente (ex: usuario_perfis)
INSERT INTO usuario_perfis (usuario_id, perfil_id)
VALUES (100, (SELECT id FROM TB_PERFIL WHERE nome = 'USER' LIMIT 1))
;