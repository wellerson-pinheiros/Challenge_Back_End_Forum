-- 1. Populando TB_TOPICO (Removi o 'id' dos dois lados)
INSERT INTO TB_TOPICO (titulo, mensagem, data_criacao, status, curso_id) VALUES
('Como criar uma classe em Java?', 'Alguém pode me ajudar a entender classes?', NOW(), 'Rascunho', 1),
('Dicas para Spring Boot', 'Melhores práticas para controllers?', NOW(), 'Rascunho', 2),
('Consultas SQL básicas', 'Como fazer JOIN entre tabelas?', NOW(), 'Rascunho', 3);

-- 2. Populando TB_RESPOSTAS (Removi o 'id' dos dois lados)
INSERT INTO TB_RESPOSTAS (mensagem, topico_id, curso_id, data_criacao, solucao) VALUES
('Você pode começar criando uma classe pública...', 1, 1, NOW(), FALSE),
('Use @RestController e organize seus endpoints.', 2, 2, NOW(), TRUE),
('Utilize INNER JOIN para unir tabelas relacionadas.', 3, 3, NOW(), FALSE);