package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Respostas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RespostaCreatDTO(
                               @NotBlank(message = "A mensagem não pode ser vazia")
                               String mensagem,
                               @NotNull
                               Long topico,
                               @NotNull
                               Long usuarioID) {

    public RespostaCreatDTO(Respostas respostas){
        this(respostas.getMensagem(), respostas.getTopico().getId(), respostas.getAutor().getId());
    }
}
