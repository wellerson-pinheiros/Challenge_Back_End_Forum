package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Curso;

import java.util.List;

public record CursoDTO(Long id, String nome, String descricao, CategoriaSimplesDTO categoriaSimplesDTO) {

    public  CursoDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getDescricao(), new CategoriaSimplesDTO(curso.getCategoria().getNome(), curso.getCategoria().getDescricao()));

    }

}
