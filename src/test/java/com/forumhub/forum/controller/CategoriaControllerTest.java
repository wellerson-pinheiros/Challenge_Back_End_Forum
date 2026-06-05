package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Categoria;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@ActiveProfiles("test")
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retorna código de erro 400 caso as informações enviadas forem inválidas")
    public void cadastro_cenario1() throws Exception {

        // Arrange
        String jsonBody =
                """    
                    {  "nome" : ""; }
                """;
        // ACTION

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/categorias")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        //Assert

        assertThat(resposta.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve retorna código 201 created ao salva uma categoria no banco de dados")
    @Transactional
    public void cadastro_cenario2() throws Exception {

    // 1. Preparamos um JSON válido (apenas com as chaves e valores)
        String jsonBody =
                """
                    {  
                        "nome" : "BackEnd",
                        "descricao"  : "Curso para te preparar para o mercado de Backend"
                     }
                """;

        // 2. Fazemos a chamada POST (a API é quem vai salvar no banco)
        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/categorias")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        // 3. Validamos se o status retornado é 201 (Created)
        assertThat(resposta.getStatus()).isEqualTo(201);
    }

    @DisplayName("Deve retorna código 204, caso seja feito o soft delete com sucesso")
    @Test
    @Transactional
    public void delete_cenario3() throws Exception {

        // arrange
        Categoria categoria = new Categoria();
        categoria.setNome("BackEnd");
        categoria.setDescricao("Curso");
        categoria.setAtivo(true);
        entityManager.persist(categoria);
        entityManager.flush();

        // action
        var resposta = mockMvc.perform(MockMvcRequestBuilders.delete("/categorias/{id}",categoria.getId()))
                .andReturn().getResponse().getStatus();

        entityManager.clear();

        // Busca a categoria atualizada do banco para validar o Soft Delete
        Categoria categoriaAtualizada = entityManager.find(Categoria.class, categoria.getId());

        // assert
        assertThat(categoria.isAtivo()).isFalse();
        assertThat(resposta).isEqualTo(204);
    }

    @Test
    @Transactional
    @DisplayName("Deve retorna código 200 se a consulta por id for um sucesso")
    public void findById_cenario4() throws Exception {
        // arrange
        Categoria categoria = new Categoria();
        categoria.setNome("BackEnd");
        categoria.setDescricao("Curso");

        entityManager.persist(categoria);
        entityManager.flush();

        // action

        // Action & Assert encadeados
        mockMvc.perform(MockMvcRequestBuilders.get("/categorias/{id}", categoria.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Equivalente ao seu isEqualTo(200)
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(categoria.getNome())) // Valida o nome em vez do ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoria.getId())); // Entra no JSON retornado e verifica se o campo "id" é igual ao esperado

        entityManager.clear();

    }

}
