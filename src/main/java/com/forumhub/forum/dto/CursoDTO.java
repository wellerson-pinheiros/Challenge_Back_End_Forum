package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Curso;

public record CursoDTO(Long id, String nome, String descricao, Long categoriaId) {

    public  CursoDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getDescricao(), curso.getCategoria().getId());

    }
}
