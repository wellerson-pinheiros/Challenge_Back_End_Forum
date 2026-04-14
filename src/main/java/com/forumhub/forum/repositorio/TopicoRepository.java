package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.Topico;
import com.forumhub.forum.domain.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository  extends JpaRepository<Topico, Long> {

     List<Topico>  findByCurso_NomeContainingIgnoreCase(String nomeCurso);

    @Modifying
    @Transactional // Importante para garantir a escrita no banco
    @Query("UPDATE Topico t SET t.usuario = :novoDono WHERE t.usuario = :antigoDono")
    void transferirTopicosParaAnonimo(Usuario antigoDono, Usuario novoDono);
}
