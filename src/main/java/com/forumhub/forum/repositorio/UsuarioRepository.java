package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

   Optional<Usuario>  findByEmail(String email);
}
