package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso,Long> {


    @Query("SELECT c FROM Curso c JOIN c.categoria cat WHERE LOWER(cat.nome) LIKE LOWER(CONCAT('%', :nomeCategoria, '%'))")
    List<Curso> findByCategoriaNomeLike(@Param("nomeCategoria") String nomeCategoria);



    Optional<Curso> findByNomeIgnoreCase(String nome);

    List<Curso> findByAtivoTrue();
}
