package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Categoria;
import com.forumhub.forum.domain.Curso;

import java.util.List;
import java.util.Set;

public record CategoriaDTO(

        Long id,
        String nome,
        String descricao,
        List<CursoDTO> cursos ) {
    public CategoriaDTO(Categoria categoria) {
        this(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getCursos() != null
                        ? categoria.getCursos().stream().map(CursoDTO::new).toList()
                        : List.of()
        );
    }
}
