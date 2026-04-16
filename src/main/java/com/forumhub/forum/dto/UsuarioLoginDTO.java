package com.forumhub.forum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioLoginDTO(
        @NotBlank
        String email,
        @NotNull
        String senha) {

}
