INSERT INTO TB_TOPICO (id, titulo, mensagem, data_criacao, status, curso_id) VALUES
(1, 'Como criar uma classe em Java?', 'Alguém pode me ajudar a entender classes?', NOW(), 'Rascunho', 1),
(2, 'Dicas para Spring Boot', 'Melhores práticas para controllers?', NOW(), 'Rascunho', 2),
(3, 'Consultas SQL básicas', 'Como fazer JOIN entre tabelas?', NOW(), 'Rascunho', 3);

INSERT INTO TB_RESPOSTAS (id, mensagem, topico_id, curso_id, datacriacao, solucao) VALUES
(1, 'Você pode começar criando uma classe pública...', 1, 1, NOW(), FALSE),
(2, 'Use @RestController e organize seus endpoints.', 2, 2, NOW(), TRUE),
(3, 'Utilize INNER JOIN para unir tabelas relacionadas.', 3, 3, NOW(), FALSE);