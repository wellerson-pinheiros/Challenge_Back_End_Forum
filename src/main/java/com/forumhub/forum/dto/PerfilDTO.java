package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Perfil;
import com.forumhub.forum.enums.PerfilEnum;
import jakarta.validation.constraints.NotBlank;

public record PerfilDTO (
                        Long id,
                        @NotBlank(message = "O nome de perfil não pode estár vazio e o nome tem que ter mais que três letras")
                        PerfilEnum nome){
    public PerfilDTO (Perfil perfil){
        this(perfil.getId(), perfil.getNome());
    }
}
