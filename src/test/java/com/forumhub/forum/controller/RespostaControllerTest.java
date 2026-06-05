package com.forumhub.forum.controller;

import com.forumhub.forum.domain.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assert;
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


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@ActiveProfiles("test")
public class RespostaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RespostasController respostasController;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar código 201 ao criar uma resposta corretamente")
    @Transactional
    public void CriarResposta() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Wellerson");
        usuario.setEmail("Wellerson@gmail.com");
        usuario.setSenha("l123456");

        entityManager.persist(usuario);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Milena");
        usuario2.setEmail("milena@gmail.com");
        usuario2.setSenha("l123456");
        entityManager.persist(usuario2);

        Categoria categoria = new Categoria();
        categoria.setNome("Criando uma categoria");
        categoria.setDescricao("Criando uma categoria");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("BackEnd");
        curso.setDescricao("BackEnd descricao");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        Topico topico =  new Topico();
        topico.setMensagem("Mensagem do tópico");
        topico.setTitulo("Titulo do tópico");
        topico.setCurso(curso);

        topico.setUsuario(usuario2);

        entityManager.persist(usuario);
        entityManager.persist(usuario2);
        entityManager.persist(topico);

        String jsonBody = """
                    {
                        "usuarioID" : %d,
                        "mensagem" : "Mensagem de teste",
                        "topico" : %d
                    }
                """.formatted(usuario.getId(), topico.getId());

        entityManager.flush();

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/respostas")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                        .andReturn().getResponse();

        entityManager.clear();

        //assert

        assertThat(resposta.getStatus()).isEqualTo(201);
    }

    @Test
    @DisplayName("Deve retornar código 204 ao atualizar uma resposta corretamente")
    @Transactional
    public void atualizarResposta() throws Exception {

        // 1. SETUP: Criar o cenário inicial no banco de dados
        Usuario autor = new Usuario();
        autor.setNome("Wellerson");
        autor.setEmail("wellerson@gmail.com");
        autor.setSenha("l123456");
        entityManager.persist(autor);

        Categoria categoria = new Categoria();
        categoria.setNome("Alguma categoria");
        categoria.setDescricao("Categoria de programação");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("BackEnd");
        curso.setDescricao("BackEnd descricao");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        Topico topico = new Topico();
        topico.setMensagem("Dúvida sobre testes");
        topico.setTitulo("Como atualizar?");
        topico.setCurso(curso);
        topico.setUsuario(autor);
        entityManager.persist(topico);

        // Criamos a resposta com a mensagem ORIGINAL
        Respostas resposta = new Respostas();
        resposta.setMensagem("Mensagem antiga que está errada e será alterada");
        resposta.setTopico(topico);
        resposta.setAutor(autor);
        resposta.setSolucao(false);
        entityManager.persist(resposta);

        // Sincroniza com o banco para garantir a geração do ID da resposta
        entityManager.flush();

        // 2. PREPARAR O JSON:
        // Assumindo que o seu AtualizarMensagemDTO receba o "id" da resposta e a nova "mensagem"
        String jsonBody = """
                {
                    "id" : %d,
                    "mensagem" : "Mensagem atualizada com sucesso através do teste!"
                }
            """.formatted(resposta.getId());

        // 3. EXECUÇÃO: Disparar a requisição PUT
        var response = mockMvc.perform(MockMvcRequestBuilders.put("/respostas")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        entityManager.flush();
        // Limpa o cache do EntityManager. Isso é crucial para forçar o Hibernate
        // a ir buscar a resposta atualizada direto no banco no próximo passo.
        entityManager.clear();

        // 4. VALIDAÇÃO (Asserts)

        // Valida se a API respondeu com 204 No Content (como definido na sua controller)
        assertThat(response.getStatus()).isEqualTo(204);

        Respostas respostaAtualizada = entityManager.find(Respostas.class, resposta.getId());
        assertThat(respostaAtualizada.getMensagem()).isEqualTo("Mensagem atualizada com sucesso através do teste!");
    }

    @Test
    @DisplayName("Deve retornar código 204 ao deletar uma resposta com sucesso")
    @Transactional
    public void deletarResposta() throws Exception {

        // 1. SETUP: Criar e salvar o cenário completo no banco de dados
        Usuario autor = new Usuario();
        autor.setNome("Wellerson");
        autor.setEmail("wellerson@gmail.com");
        autor.setSenha("l123456");
        entityManager.persist(autor);

        Categoria categoria = new Categoria();
        categoria.setNome("Programação na linguagem portugol");
        categoria.setDescricao("Categoria de programação");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("BackEnd");
        curso.setDescricao("BackEnd descricao");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        Topico topico = new Topico();
        topico.setMensagem("Dúvida sobre deleção");
        topico.setTitulo("Como deletar?");
        topico.setCurso(curso);
        topico.setUsuario(autor);
        entityManager.persist(topico);

        // Criamos a resposta que será deletada
        Respostas resposta = new Respostas();
        resposta.setMensagem("Mensagem que não deveria mais existir");
        resposta.setTopico(topico);
        resposta.setAutor(autor);
        resposta.setSolucao(false);
        entityManager.persist(resposta);

        // Sincroniza com o banco para garantir a geração do ID da resposta
        entityManager.flush();

        // Guardamos o ID real que o banco gerou para usar na requisição
        Long idDaResposta = resposta.getId();

        // 2. EXECUÇÃO: Disparar a requisição DELETE para /respostas/{id}
        var response = mockMvc.perform(MockMvcRequestBuilders.delete("/respostas/{id}", idDaResposta))
                .andReturn().getResponse();

        // Limpa o cache do EntityManager para a validação final
        entityManager.clear();

        // 3. VALIDAÇÃO (Asserts)

        // Valida se a Controller respondeu com o status correto (204 No Content)
        assertThat(response.getStatus()).isEqualTo(204);

    }


    @Test
    @DisplayName("Deve retornar código 400 Bad Request ao tentar criar uma resposta com mensagem vazia")
    public void criarRespostaComMensagemInvalida() throws Exception {

        // Enviando uma string vazia na mensagem
        String jsonInvalido = """
                {
                    "usuarioID" : 1,
                    "mensagem" : "", 
                    "topico" : 1
                }
            """;
        // Executa a requisição POST
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/respostas")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonInvalido))
                .andReturn().getResponse();

        // Valida se o Spring bloqueou a requisição com o status 400
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve retornar código 400 Bad Request ao tentar criar uma resposta com um tópico que não existe ")
    public void criarRespostaComMensagemInvalidaCenario3() throws Exception {

        // Enviando uma string vazia na mensagem
        String jsonInvalido = """
                {
                    "usuarioID" : 1,
                    "mensagem" : "Mensagem de teste para testar o teste que foi implementado kkkkk", 
                    "topico" : 3333333333
                }
            """;
        // Executa a requisição POST
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/respostas")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonInvalido))
                .andReturn().getResponse();

        // Valida se o Spring bloqueou a requisição com o status 400
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Deve retornar código 400 Bad Request ao tentar criar uma resposta sem o tópico")
    public void criarRespostaComMensagemInvalidaCenario2() throws Exception {

        // Enviando uma string vazia na mensagem
        String jsonInvalido = """
                {
                    "usuarioID" : 1,
                    "mensagem" : "", 
                    "topico" : 0
                }
            """;
        // Executa a requisição POST
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/respostas")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonInvalido))
                .andReturn().getResponse();

        // Valida se o Spring bloqueou a requisição com o status 400
        assertThat(response.getStatus()).isEqualTo(400);
    }
}

