package com.forumhub.forum.controller;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.post;

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
    @DisplayName("Deve retorna código de erro 400 caso quando as informações enviadas forem inválidas")
    public void cadastro_cenario1() throws Exception {

        // Arrange
        String jsonBody = """
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



}
