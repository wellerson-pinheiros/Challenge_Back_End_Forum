package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Respostas;
import jakarta.validation.constraints.NotNull;

public record DeletarMensagemDTO(
        @NotNull
        Long id) {

}
