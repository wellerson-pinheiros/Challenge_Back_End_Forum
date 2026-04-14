package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.Respostas;
import com.forumhub.forum.domain.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RespostaRepository extends JpaRepository<Respostas, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Respostas r SET r.autor = :novoDono WHERE r.autor = :antigoDono")
    void transferirRespostasParaAnonimo(Usuario antigoDono, Usuario novoDono);
}
