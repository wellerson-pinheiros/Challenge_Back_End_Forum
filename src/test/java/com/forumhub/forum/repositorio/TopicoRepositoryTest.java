package com.forumhub.forum.repositorio;

import com.forumhub.forum.domain.*;
import com.forumhub.forum.dto.UsuarioCreatDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TopicoRepositoryTest {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Esse teste deve transferir todas os tópicos de um usário excluido do banco para o usuario anônimo para não quebrar o banco de dados" )
    void deveriaTransferirTopicosParaAnonimo() {
        // 1. Mínimo de Usuários
        Usuario antigoDono = persistirUsuario("Antigo", "antigo@email.com","Senha123");
        Usuario novoDono = persistirUsuario("Anonimo", "anon@email.com","Senha123");

        //criando tabela categoria
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria Teste");
        entityManager.persist(categoria);

        // criando a tabela curso
        Curso curso = new Curso();
        curso.setNome(antigoDono.getNome());
        curso.setCategoria(categoria);
        entityManager.persist(curso);

        // 2. Mínimo de Tópico (Necessário para a Resposta existir)
        Topico topico = new Topico();
        topico.setTitulo("Titulo");
        topico.setMensagem("Mensagem");
        topico.setCurso(curso);
        topico.setUsuario(antigoDono); // Dono do tópico
        entityManager.persist(topico);

        // 3. Criar a Resposta vinculada ao Antigo Dono
        Respostas r1 = new Respostas();
        r1.setMensagem("Resposta 1");
        r1.setAutor(antigoDono);
        r1.setTopico(topico);
        entityManager.persist(r1);

        // 4. AÇÃO: O método que você quer testar
       repository.transferirTopicosParaAnonimo(antigoDono, novoDono);

        // Força o banco a atualizar e limpa o cache da memória (CRITICAL)
        entityManager.flush();
        entityManager.clear();

        // 5. VERIFICAÇÃO (Assert)
        Topico topicoAtualizada = entityManager.find(Topico.class, topico.getId());
        assertEquals(novoDono.getId(), topicoAtualizada.getUsuario().getId());
    }

    private Usuario persistirUsuario(String nome, String email,String senha) {
        UsuarioCreatDTO usuarioCreatDTO = new UsuarioCreatDTO(nome,email,senha);
        Usuario u = new Usuario(usuarioCreatDTO);
        return entityManager.persist(u);
    }

}
