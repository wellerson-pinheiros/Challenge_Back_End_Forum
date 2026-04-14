package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Respostas;
import com.forumhub.forum.domain.Topico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RespostasDTO(Long id,
                           @NotBlank(message = "A mensagem não pode ser vazia")
                           String mensagem,
                           @NotNull
                           Long topico,

                           Instant dataCriacao,

                           Boolean solucao,
                           Long usuarioID) {

    public RespostasDTO(Respostas respostas){
        this(respostas.getId(),respostas.getMensagem(), respostas.getTopico().getId(), respostas.getDataCriacao(), respostas.getSolucao(),respostas.getAutor().getId());
    }
}
