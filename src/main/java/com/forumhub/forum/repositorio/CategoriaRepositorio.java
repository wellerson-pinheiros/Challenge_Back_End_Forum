package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepositorio extends JpaRepository<Categoria,Long> {

   Optional<Categoria> findByNome(String nome);
   List <Categoria> findByAtivoTrue();
}
