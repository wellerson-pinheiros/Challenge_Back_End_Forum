CREATE TABLE TB_CURSOS (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    descricao VARCHAR(300),
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES TB_CATEGORIAS(id)
        ON DELETE CASCADE
);