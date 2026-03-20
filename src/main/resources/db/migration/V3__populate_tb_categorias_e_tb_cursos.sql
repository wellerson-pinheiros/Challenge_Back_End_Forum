-- Populando TB_CATEGORIAS
INSERT INTO tb_categorias (nome, descricao) VALUES
('Programação', 'Cursos relacionados a desenvolvimento de software'),
('Design', 'Cursos de design gráfico e UX/UI'),
('Marketing', 'Cursos de marketing digital e publicidade'),
('Banco de Dados', 'Cursos de SQL, NoSQL e administração de banco de dados'),
('Inteligência Artificial', 'Cursos de IA, machine learning e deep learning');

-- Populando TB_CURSOS
INSERT INTO tb_cursos (nome, descricao, categoria_id) VALUES
('Java Básico', 'Introdução à linguagem Java', (SELECT id FROM tb_categorias WHERE nome = 'Programação')),
('Java Avançado', 'Conceitos avançados e boas práticas em Java', (SELECT id FROM tb_categorias WHERE nome = 'Programação')),
('React.js', 'Desenvolvimento Front-End com React', (SELECT id FROM tb_categorias WHERE nome = 'Programação')),
('Adobe Photoshop', 'Manipulação de imagens e design gráfico', (SELECT id FROM tb_categorias WHERE nome = 'Design')),
('Figma', 'Design de interfaces com Figma', (SELECT id FROM tb_categorias WHERE nome = 'Design')),
('SEO e Marketing de Conteúdo', 'Técnicas de SEO e marketing digital', (SELECT id FROM tb_categorias WHERE nome = 'Marketing')),
('SQL para Iniciantes', 'Aprenda SQL do zero', (SELECT id FROM tb_categorias WHERE nome = 'Banco de Dados')),
('PostgreSQL Avançado', 'Administração e otimização de PostgreSQL', (SELECT id FROM tb_categorias WHERE nome = 'Banco de Dados')),
('Machine Learning com Python', 'Introdução a ML usando Python', (SELECT id FROM tb_categorias WHERE nome = 'Inteligência Artificial')),
('Deep Learning com TensorFlow', 'Redes neurais profundas com TensorFlow', (SELECT id FROM tb_categorias WHERE nome = 'Inteligência Artificial'));