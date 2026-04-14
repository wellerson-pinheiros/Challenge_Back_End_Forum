package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Curso;
import com.forumhub.forum.domain.Respostas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CursoDTO(

        Long id,
        @NotBlank(message = "O nome não pode ser nulo")
        String nome,
        String descricao,
        CategoriaSimplesDTO categoriaSimplesDTO) {

    public  CursoDTO(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getDescricao(), new CategoriaSimplesDTO(curso.getCategoria().getNome(), curso.getCategoria().getDescricao()));

    }


}
