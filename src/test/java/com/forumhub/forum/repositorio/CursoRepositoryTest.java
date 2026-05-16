package com.forumhub.forum.repositorio;

import com.forumhub.forum.excecoes.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CursoRepositoryTest {

    @Autowired
    CursoRepository cursoRepository;



    @Test
    @DisplayName("Deve retornar uma lista de  cursos pela categoria do curso")
    void findByCategoriaNomeLike() {

        // 1. ARRANGE (Preparar)
        String nome = "Programação";

        // 2. ACT (Agir)

       var resultadoBuscadoRepositorio =  cursoRepository.findByCategoriaNomeLike(nome);

       // 3. Assert (Verificar)
        assertNotNull(resultadoBuscadoRepositorio);
        assertFalse(resultadoBuscadoRepositorio.isEmpty());
    }
    
}