package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Respostas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record AtualizarMensagemDTO(Long id,
                           @NotBlank(message = "A mensagem não pode ser vazia")
                           String mensagem,


                           Instant dataCriacao,

                           Boolean solucao) {

    public AtualizarMensagemDTO(Respostas respostas){
        this(respostas.getId(),respostas.getMensagem(), respostas.getDataCriacao(), respostas.getSolucao());
    }
}
