package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Categoria;
import com.forumhub.forum.domain.Curso;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@ActiveProfiles("test")
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;


    @Test
    @DisplayName("Deve retorna código de erro 400 caso quando as informações enviadas forem inválidas")
    void cadastrar_cenario1() throws Exception {
     var resposta =  mockMvc.perform(post("/cursos"))
               .andReturn().getResponse();

        assertThat(resposta.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve retorna código 201 caso os dados de cadastro estiverem tudo certo")
    @Transactional
    void cadastrar_cenario2() throws Exception {
        // 1. ARRANGE (Preparar)
        Categoria categoria = new Categoria();
        categoria.setNome("BackEnd");
        categoria.setDescricao("Curso para te preparar para o mercado de Backend com Java e Spring Boot");
        entityManager.persist(categoria);

        // Aqui criamos os dados que o cliente enviaria no formato JSON
        String jsonBody = """
                    {
                    "nome": "Curso BackEnd com  Java e Spring Boot ",
                    "descricao": "Curso para Iniciantes de progamação que deseja entrar no mundo da programaçao, e desenvolver projetos utilizando Java e Spring Boot",
                    "categoriaSimplesDTO" : {
                        "nome" : "BackEnd"
                    }
                   }
                """;

        //Action (Enviando a requisição POST com o corpo JSON)
        var resposta = mockMvc.perform(post("/cursos")
                    .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content(jsonBody))
                        .andReturn().getResponse();


        //assert (aqui é onde esperamos que o resultado seja o esperado)
        assertThat(resposta.getStatus()).isEqualTo(201);
    }

    @Test
    @DisplayName("Deve retorna código 409 se o nome do curso já existir no banco de dados, Não pode ter dois cursos com o mesmo nome ou mesmo id")
    @Transactional
    void cadastrar_cenario3() throws Exception {
        // 1. ARRANGE (Preparar)
        Categoria categoria = new Categoria();
        categoria.setNome("BackEnd");
        categoria.setDescricao("Curso para te preparar para o mercado de Backend com Java e Spring Boot");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("Curso BackEnd com  Java e Spring Boot");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        // Aqui criamos os dados que o cliente enviaria no formato JSON
        String jsonBody = """
                    {
                    "nome": "Curso BackEnd com  Java e Spring Boot",
                    "descricao": "Curso para Iniciantes de progamação que deseja entrar no mundo da programaçao, e desenvolver projetos utilizando Java e Spring Boot",
                    "categoriaSimplesDTO" : {
                        "nome" : "BackEnd"
                    }
                   }
                """;

        //Action (Enviando a requisição POST com o corpo JSON)
        var resposta = mockMvc.perform(post("/cursos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();


        //assert (aqui é onde esperamos que o resultado seja o esperado)
        assertThat(resposta.getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("Testar se ao deletar um objeto no banco retorna 204 no contente")
    @Transactional
    void cadastrar_cenario4() throws Exception {
        // 1. ARRANGE (Preparar)
        Categoria categoria = new Categoria();
        categoria.setNome("BackEnd");
        categoria.setDescricao("Curso para te preparar para o mercado de Backend com Java e Spring Boot");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("Curso BackEnd com  Java e Spring Boot");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        //Action e //assert (passamos o ID do curso diretamente como argumento da URL)
        var resposta = mockMvc.perform(delete("/cursos/{id}", curso.getId()))
                .andExpect(status().isNoContent()); // Valida se retornou 204 No Content
    }

    @Test
    @DisplayName("Testar se ao deletar um objeto no banco retorna 404 curso não existe")
    @Transactional
    void cadastrar_cenario5() throws Exception {

        //arrange
        Long idQueNaoExiste = 999l;

        //Action e //assert (passamos o ID do curso diretamente como argumento da URL)
        var resposta = mockMvc.perform(delete("/cursos/{id}", idQueNaoExiste))
                .andReturn().getResponse();

        assertThat(resposta.getStatus()).isEqualTo(404);
    }



}
