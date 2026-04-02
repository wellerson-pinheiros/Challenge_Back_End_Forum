package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository  extends JpaRepository<Topico, Long> {

     List<Topico>  findByCurso_NomeContainingIgnoreCase(String nomeCurso);
}
