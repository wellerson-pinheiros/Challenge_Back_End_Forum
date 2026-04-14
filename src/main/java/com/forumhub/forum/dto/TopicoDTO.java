package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Topico;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record TopicoDTO(Long id,
                        @NotBlank(message = "O titulo não pode ser vazio.")
                        String titulo,
                        String mensagem,
                        Instant dataCriacao,
                        Enum status ,
                        Long cursoId,
                        Long usuarioId) {

    public TopicoDTO(Topico topico) {
        this(topico.getId(),topico.getTitulo(),topico.getMensagem(),topico.getDataCriacao(),topico.getStatus(),topico.getCurso().getId(),topico.getUsuario().getId());
    }
}
