package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.Respostas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<Respostas, Long> {
}
