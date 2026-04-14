package com.forumhub.forum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UsuarioCreatDTO (
        @NotBlank(message = "O nome do usuário é obrigatório")
        String nome,
        @Email
        @NotNull
        String email,
        @NotNull
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{3,}$",
                message = "A senha deve ter no mínimo 3 caracteres, contendo pelo menos uma letra e um número"
        )
        String senha){
}
