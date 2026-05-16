package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Curso;
import jakarta.validation.constraints.NotBlank;

public record CursoCreatDTO(


        @NotBlank(message = "O nome não pode ser nulo")
        String nome,
        String descricao,
        CategoriaSimplesDTO categoriaSimplesDTO) {

    public CursoCreatDTO(Curso curso) {
        this(curso.getNome(), curso.getDescricao(), new CategoriaSimplesDTO(curso.getCategoria().getNome(), curso.getCategoria().getDescricao()));

    }


}
