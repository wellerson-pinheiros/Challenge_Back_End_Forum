package com.forumhub.forum.controller;



import com.forumhub.forum.domain.Categoria;
import com.forumhub.forum.domain.Curso;
import com.forumhub.forum.domain.Topico;
import com.forumhub.forum.domain.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@ActiveProfiles("test")
public class TopicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar código 201 Created ao criar um tópico com dados válidos")
    @Transactional
    public void criarTopicoComSucesso() throws Exception {

        // 1. SETUP: Criar e salvar dependências
        Usuario autor = new Usuario();
        autor.setNome("Wellerson");
        autor.setEmail("wellerson@gmail.com");
        autor.setSenha("l123456");
        entityManager.persist(autor);

        Categoria categoria = new Categoria();
        categoria.setNome("Teste");
        categoria.setDescricao("Categoria de TI");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("Spring Boot Testes");
        curso.setDescricao("Aprenda a testar APIs");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        entityManager.flush(); // Garante que os IDs foram gerados

        // 2. PREPARAR O JSON
        // Atenção aos nomes: cursoId e usuarioId (iguais ao seu TopicoDTO)
        String jsonBody = """
            {
                "titulo" : "Dúvida sobre MockMvc",
                "mensagem" : "Como testar requisições POST?",
                "cursoId" : %d,
                "usuarioId" : %d
            }
        """.formatted(curso.getId(), autor.getId());

        // 3. EXECUÇÃO
        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/topicos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        // 4. VALIDAÇÃO
        // A sua controller retorna ResponseEntity.created(), então o status é 201!
        assertThat(resposta.getStatus()).isEqualTo(201);
    }

    @Test
    @DisplayName("Deve retornar código 400 Bad Request ao tentar criar um tópico sem título")
    public void criarTopicoSemTitulo() throws Exception {

        // JSON com a mensagem e os IDs corretos, mas título VAZIO
        String jsonInvalido = """
            {
                "titulo" : "", 
                "mensagem" : "Corpo da mensagem válida",
                "cursoId" : 1,
                "usuarioId" : 1
            }
        """;

        // Execução (Não precisamos persistir nada no banco, pois o @Valid barra antes)
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/topicos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonInvalido))
                .andReturn().getResponse();

        // Validação
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve retornar código 400 Bad Request ao tentar criar um tópico sem mensagem")
    public void criarTopicoSemMensagem() throws Exception {

        // JSON com a mensagem e os IDs corretos, mas título VAZIO
        String jsonInvalido = """
            {
                "titulo" : "Titulo do tópico", 
                "mensagem" : "",
                "cursoId" : 1,
                "usuarioId" : 1
            }
        """;

        // Execução (Não precisamos persistir nada no banco, pois o @Valid barra antes)
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/topicos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonInvalido))
                .andReturn().getResponse();

        // Validação
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve retornar código 204 ao deletar um tópico com sucesso")
    @Transactional
    public void deletarTopicoComSucesso() throws Exception {

        // 1. SETUP: Criar o cenário e o próprio tópico no banco
        Usuario autor = new Usuario();
        autor.setNome("Milena");
        autor.setEmail("milena@gmail.com");
        autor.setSenha("l123456");
        entityManager.persist(autor);

        Categoria categoria = new Categoria();
        categoria.setNome("FrontEnd");
        categoria.setDescricao("Cursos de Front");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("React");
        curso.setDescricao("React do zero");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        Topico topico = new Topico();
        topico.setTitulo("Erro no useEffect");
        topico.setMensagem("Meu componente renderiza duas vezes");
        topico.setCurso(curso);
        topico.setUsuario(autor);
        entityManager.persist(topico);

        entityManager.flush();

        Long idDoTopico = topico.getId();

        // 2. EXECUÇÃO
        var response = mockMvc.perform(delete("/topicos/{id}", idDoTopico))
                .andReturn().getResponse();

        entityManager.clear();

        // 3. VALIDAÇÃO
        assertThat(response.getStatus()).isEqualTo(204);

    }

    @Test
    @DisplayName("Deve retornar 204 e atualizar tanto o título quanto a mensagem do tópico")
    @Transactional
    public void atualizarTituloEMensagem() throws Exception {
        Topico topicoSalvo = prepararTopicoNoBanco();

        String jsonBody = """
            {
                "id" : %d,
                "titulo" : "Título TOTALMENTE Novo",
                "mensagem" : "Mensagem TOTALMENTE Nova",
                "cursoId" : %d,
                "usuarioId" : %d
            }
        """.formatted(topicoSalvo.getId(), topicoSalvo.getCurso().getId(), topicoSalvo.getUsuario().getId());

        var response = mockMvc.perform(MockMvcRequestBuilders.put("/topicos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();
        entityManager.flush();

        entityManager.clear();

        assertThat(response.getStatus()).isEqualTo(204);

        // Verifica se os DOIS mudaram
        Topico topicoAtualizado = entityManager.find(Topico.class, topicoSalvo.getId());
        assertThat(topicoAtualizado.getTitulo()).isEqualTo("Título TOTALMENTE Novo");
        assertThat(topicoAtualizado.getMensagem()).isEqualTo("Mensagem TOTALMENTE Nova");
    }

    @Test
    @DisplayName("Deve retornar 204 e atualizar APENAS o título do tópico")
    @Transactional
    public void atualizarApenasTitulo() throws Exception {
        Topico topicoSalvo = prepararTopicoNoBanco();

        // Mandamos um título NOVO, mas mantemos a mensagem ANTIGA no JSON
        String jsonBody = """
            {
                "id" : %d,
                "titulo" : "Apenas o Título Mudou",
                "mensagem" : "Mensagem Antiga Original",
                "cursoId" : %d,
                "usuarioId" : %d
            }
        """.formatted(topicoSalvo.getId(), topicoSalvo.getCurso().getId(), topicoSalvo.getUsuario().getId());

        var response = mockMvc.perform(MockMvcRequestBuilders.put("/topicos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();

        entityManager.flush();
        entityManager.clear();

        assertThat(response.getStatus()).isEqualTo(204);
        Topico topicoAtualizado = entityManager.find(Topico.class, topicoSalvo.getId());

        // Verifica se o título mudou, mas a mensagem continuou a mesma
        assertThat(topicoAtualizado.getTitulo()).isEqualTo("Apenas o Título Mudou");
        assertThat(topicoAtualizado.getMensagem()).isEqualTo("Mensagem Antiga Original");
    }

    @Test
    @DisplayName("Deve retornar 204 e atualizar APENAS a mensagem do tópico")
    @Transactional
    public void atualizarApenasMensagem() throws Exception {
        Topico topicoSalvo = prepararTopicoNoBanco();

        // Mandamos a mensagem NOVA, mas mantemos o título ANTIGO no JSON
        String jsonBody = """
            {
                "id" : %d,
                "titulo" : "Título Antigo Original",
                "mensagem" : "Apenas a Mensagem Mudou",
                "cursoId" : %d,
                "usuarioId" : %d
            }
        """.formatted(topicoSalvo.getId(), topicoSalvo.getCurso().getId(), topicoSalvo.getUsuario().getId());

        var response = mockMvc.perform(MockMvcRequestBuilders.put("/topicos")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody))
                .andReturn().getResponse();
        entityManager.flush();
        entityManager.clear();

        assertThat(response.getStatus()).isEqualTo(204);

        Topico topicoAtualizado = entityManager.find(Topico.class, topicoSalvo.getId());

        // Verifica se a mensagem mudou, mas o título continuou o mesmo
        assertThat(topicoAtualizado.getMensagem()).isEqualTo("Apenas a Mensagem Mudou");
        assertThat(topicoAtualizado.getTitulo()).isEqualTo("Título Antigo Original");
    }

    private Topico prepararTopicoNoBanco() {
        Usuario autor = new Usuario();
        autor.setNome("Wellerson");
        autor.setEmail("wellerson@gmail.com");
        autor.setSenha("l123456");
        entityManager.persist(autor);

        Categoria categoria = new Categoria();
        categoria.setNome("BackEnd");
        categoria.setDescricao("Cursos de BackEnd");
        entityManager.persist(categoria);

        Curso curso = new Curso();
        curso.setNome("Spring Boot");
        curso.setDescricao("API REST");
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        Topico topico = new Topico();
        topico.setTitulo("Título Antigo Original");
        topico.setMensagem("Mensagem Antiga Original");
        topico.setCurso(curso);
        topico.setUsuario(autor);
        entityManager.persist(topico);

        entityManager.flush();
        return topico;
    }
}
