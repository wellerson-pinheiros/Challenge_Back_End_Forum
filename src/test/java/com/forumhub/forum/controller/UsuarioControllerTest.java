package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Usuario;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@ActiveProfiles("test")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar código 201 Created ao criar um usuario com dados válidos")
    @Transactional
    public void criarUsuarioComSucesso() throws Exception{
        // Aqui criamos os dados que o cliente enviaria no formato JSON
        String jsonBody = """
                    {
                    "nome": "Milena Reis dos Santos",
                    "email": "milenaReis@gmail.com",
                    "senha": "Senha123"
                   }
                """;

        //Action (Enviando a requisição POST com o corpo JSON)
        var resposta = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        //assert (aqui é onde esperamos que o resultado seja o esperado)
        assertThat(resposta.getStatus()).isEqualTo(201);

    }

    @Test
    @DisplayName("Deve retornar código 404 Bad request ao criar um usuario com dados que já existe")
    @Transactional
    public void criarUsuarioComFalha() throws Exception{
        // Aqui criamos os dados que o cliente enviaria no formato JSON

        Usuario usuario = new Usuario();
        usuario.setNome("Milena Reis dos Santos");
        usuario.setEmail("milenaReis@gmail.com");
        usuario.setSenha("Senha123");
        entityManager.persist(usuario);

        String jsonBody = """
                    {
                    "nome": "Milena Reis dos Santos",
                    "email": "milenaReis@gmail.com",
                    "senha": "Senha123"
                   }
                """;

        //Action (Enviando a requisição POST com o corpo JSON)
        var resposta = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        //assert (aqui é onde esperamos que o resultado seja o esperado)
        assertThat(resposta.getStatus()).isEqualTo(409);

    }

    @Test
    @DisplayName("Deve retornar código 400 Bad request ao criar um usuario com dados invalidos")
    @Transactional
    public void criarUsuarioComDadosErrados() throws Exception{

        String jsonBody = """
                    {
                    "nome": "Milena Reis dos Santos",
                    "email": "milenaReis",
                    "senha": "Senha123"
                   }
                """;

        //Action (Enviando a requisição POST com o corpo JSON)
        var resposta = mockMvc.perform(post("/usuarios/cadastrar")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        //assert (aqui é onde esperamos que o resultado seja o esperado)
        assertThat(resposta.getStatus()).isEqualTo(400);

    }


    @Test
    @DisplayName("Deve retornar código 204 ao atualizar  o usuário")
    @Transactional
    public void atualizarUsuario() throws Exception{

        Usuario usuario = new Usuario();
        usuario.setNome("Milena Reis dos Santos");
        usuario.setEmail("milenaReis@gmail.com");
        usuario.setSenha("Senha123");
        entityManager.persist(usuario);

        entityManager.flush();

        String jsonBody = """
                    {
                    "id": %d,
                    "nome": "Milena Reis dos Santos Atualizada"
                   }
                """.formatted(usuario.getId());

        //Action (Enviando a requisição POST com o corpo JSON)
        var resposta = mockMvc.perform(put("/usuarios")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        //assert (aqui é onde esperamos que o resultado seja o esperado)
        assertThat(resposta.getStatus()).isEqualTo(204);

    }

    @Test
    @DisplayName("Deve retornar código 404 por não encontrar o usuario com o id especifico")
    @Transactional
    public void atualizarUsuarioComErro() throws Exception{
        // 1. Arrange
       Long idInexistente = 5000L;

        String jsonBody = """
                {
                "id": %d, 
                "nome": "Usuário Teste Falha"
               }
            """.formatted(idInexistente);

        // 2. Action: Enviando a requisição PUT
        var resposta = mockMvc.perform(put("/usuarios")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        //assert (aqui é onde esperamos que o resultado seja o esperado)
        assertThat(resposta.getStatus()).isEqualTo(404);

    }



    @Test
    @DisplayName("Deve retornar código 204 ao excluir um usuário existente")
    @Transactional
    @WithMockUser // Simula o usuário logado para a segurança
    public void excluirUsuarioComSucesso() throws Exception {

        // 1. Arrange: Criamos e salvamos um usuário no banco para poder deletar
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Para Deletar");
        usuario.setEmail("deletar@gmail.com");
        usuario.setSenha("Senha123");
        entityManager.persist(usuario);
        entityManager.flush(); // Garante que o ID foi gerado

        // 2. Action: Enviando a requisição DELETE passando o ID na URL
        var resposta = mockMvc.perform(delete("/usuarios/{id}", usuario.getId()))
                .andReturn().getResponse();

        // 3. Assert: Esperamos que retorne 204 No Content
        assertThat(resposta.getStatus()).isEqualTo(204);
    }

    @Test
    @DisplayName("Deve retornar código 404 ao tentar excluir um usuário que não existe")
    @WithMockUser
    public void excluirUsuarioInexistente() throws Exception {

        // 1. Arrange: Inventamos um ID que sabemos que não está no banco
        Long idInexistente = 9999L;

        // 2. Action: Enviando a requisição DELETE com o ID falso
        var resposta = mockMvc.perform(delete("/usuarios/{id}", idInexistente))
                .andReturn().getResponse();

        // 3. Assert: Esperamos que o sistema não encontre e retorne 404
        assertThat(resposta.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Deve retornar código 200 e uma lista de usuários ao buscar todos")
    @Transactional
    @WithMockUser
    public void buscarTodosUsuariosComSucesso() throws Exception {

        // Arrange: Criando dois usuários para popular o banco
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Usuário Um");
        usuario1.setEmail("um@gmail.com");
        usuario1.setSenha("123456");
        entityManager.persist(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Usuário Dois");
        usuario2.setEmail("dois@gmail.com");
        usuario2.setSenha("123456");
        entityManager.persist(usuario2);

        entityManager.flush();

        // Action: Requisição GET para a raiz da rota
        var resposta = mockMvc.perform(get("/usuarios"))
                .andReturn().getResponse();

        // Assert: Verifica se deu sucesso (200) e se a resposta contém os dados
        assertThat(resposta.getStatus()).isEqualTo(200);
        assertThat(resposta.getContentAsString()).contains("Usuário Um");
        assertThat(resposta.getContentAsString()).contains("Usuário Dois");
    }

    @Test
    @DisplayName("Deve retornar código 200 e os dados do usuário ao buscar por um ID válido")
    @Transactional
    @WithMockUser
    public void buscarUsuarioPorIdComSucesso() throws Exception {

        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos Silva");
        usuario.setEmail("carlos@gmail.com");
        usuario.setSenha("senhaSegura");
        entityManager.persist(usuario);
        entityManager.flush();

        // Action: Passando o ID real na URL
        var resposta = mockMvc.perform(get("/usuarios/{id}", usuario.getId()))
                .andReturn().getResponse();

        // Assert
        assertThat(resposta.getStatus()).isEqualTo(200);
        assertThat(resposta.getContentAsString()).contains("Carlos Silva");
    }

    @Test
    @DisplayName("Deve retornar código 404 ao buscar por um ID que não existe")
    @WithMockUser
    public void buscarUsuarioPorIdInexistente() throws Exception {

        // Arrange: ID falso
        Long idFalso = 9999L;

        // Action
        var resposta = mockMvc.perform(get("/usuarios/{id}", idFalso))
                .andReturn().getResponse();

        // Assert
        assertThat(resposta.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Deve retornar código 200 ao buscar usuário por um e-mail válido")
    @Transactional
    @WithMockUser
    public void buscarUsuarioPorEmailComSucesso() throws Exception {

        // Arrange
        String emailBusca = "marcos.teste@gmail.com";
        Usuario usuario = new Usuario();
        usuario.setNome("Marcos Teste");
        usuario.setEmail(emailBusca);
        usuario.setSenha("senha123");
        entityManager.persist(usuario);
        entityManager.flush();

        // Action: Requisição GET para a rota específica de e-mail
        var resposta = mockMvc.perform(get("/usuarios/email/{email}", emailBusca))
                .andReturn().getResponse();

        // Assert
        assertThat(resposta.getStatus()).isEqualTo(200);
    }
}
