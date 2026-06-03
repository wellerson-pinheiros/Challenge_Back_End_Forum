package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Categoria;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao) {
    public CategoriaResponseDTO(Categoria categoria) {
       this(categoria.getId(), categoria.getNome(), categoria.getDescricao());
    }


}
