package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaSimplesDTO (


        @NotBlank(message = "O nome não pode ser nulo")
        String nome,
        @Size(max = 300, message = "Descrição deve ter no máximo 300 caracteres" )
        String descricao) {
    public CategoriaSimplesDTO (Categoria categoria) {
       this(categoria.getNome(), categoria.getDescricao());
    }


}
