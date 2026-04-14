package com.forumhub.forum.dto;

import com.forumhub.forum.domain.Perfil;
import com.forumhub.forum.domain.Usuario;
import com.forumhub.forum.enums.PerfilEnum;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record UsuarioDTO (Long id, String nome, Set<PerfilEnum> perfis) {
    // Construtor para converter a Entidade Usuario para o DTO facilmente
    public UsuarioDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getPerfis()
                        .stream()
                        .map(p -> p.getNome()) // Pega o Enum dentro de cada objeto Perfil
                        .collect(Collectors.toSet())
        );
    }
}