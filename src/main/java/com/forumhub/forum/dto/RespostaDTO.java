package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Curso;
import com.forumhub.forum.domain.Respostas;
import com.forumhub.forum.domain.Topico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RespostaDTO (Long id,
                           @NotBlank(message = "A mensagem não pode ser vazia")
                           String mensagem,
                           @NotNull
                           Topico topico,
                           @NotNull
                           Instant dataCriacao,

                           Boolean solucao) {

    public RespostaDTO(Respostas respostas){
        this(respostas.getId(),respostas.getMensagem(), respostas.getTopico(), respostas.getDatacriacao(), respostas.getSolucao());
    }
}
