package com.forumhub.forum.controller;


import com.forumhub.forum.domain.Perfil;
import com.forumhub.forum.domain.Usuario;
import com.forumhub.forum.enums.PerfilEnum;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@ActiveProfiles("test")
public class PerfilControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve remover o perfil de acesso do usuário permanentemente e checar se o usuario ainda continua sendo um usuario padrão sem os previlégios de um adiministrador")
    @Transactional
    public void removerPerfilDoUsuario() throws Exception {

        // Arrage

        //aqui nós buscamos no banco de dados porque eles já são enseridos por padrão no banco de dados, se eu for fazer um persisti dará erro de unicidade!
        Perfil perfilAdmin = entityManager.find(Perfil.class, PerfilEnum.ADMIN.getId());
        Perfil perfilUser = entityManager.find(Perfil.class, PerfilEnum.USER.getId());

        Usuario usuario = new Usuario();
        usuario.setNome("Wellerson");
        usuario.setEmail("Usuario@hotmail.com");
        usuario.setSenha("123456");
        usuario.addPerfil(perfilAdmin);
        usuario.addPerfil(perfilUser);
        entityManager.persist(usuario);
        entityManager.flush();

        // Action

        mockMvc.perform(MockMvcRequestBuilders.delete("/perfils/usuarios/{usuarioId}/perfis/{perfilId}", usuario.getId(), perfilAdmin.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        // --- ASSERT ---
        // Vai até o banco de dados buscar o usuário novamente
        Usuario usuarioAtualizado = entityManager.find(Usuario.class, usuario.getId());

        // Verifica se a lista NÃO contém mais o perfil ADMIN
        assertThat(usuarioAtualizado.getPerfis()).doesNotContain(perfilAdmin);

        // Verifica se a lista AINDA contém o perfil USER
        assertThat(usuarioAtualizado.getPerfis()).contains(perfilUser);
    }

    @Test
    @DisplayName("deve retorna código 200 ao adicionar um novo perfil ao usário")
    @Transactional
    public void adicionarPerfilAoUsuario() throws Exception {

        //Arrange

        Perfil perfilAdmin = entityManager.find(Perfil.class, PerfilEnum.ADMIN.getId());
        Perfil perfilUser = entityManager.find(Perfil.class, PerfilEnum.USER.getId());
        Perfil perfilModerator = entityManager.find(Perfil.class, PerfilEnum.MODERATOR.getId());

        Usuario usuario = new Usuario();
        usuario.setNome("Wellerson");
        usuario.setEmail("Usuario@hotmail.com");
        usuario.setSenha("123456");
        usuario.addPerfil(perfilUser);
        entityManager.persist(usuario);

        entityManager.flush();
        // ACT

        mockMvc.perform(MockMvcRequestBuilders.put("/perfils/perfis/{perfilId}/usuarios/{usuarioId}", perfilAdmin.getId(), usuario.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        Usuario usuarioAtualizado = entityManager.find(Usuario.class, usuario.getId());

            //Assert

        assertThat(usuarioAtualizado.getPerfis()).contains(perfilAdmin);
        assertThat(usuarioAtualizado.getPerfis()).contains(perfilUser);
    }

    @Test
    @DisplayName("deve retorna código 200 ao buscar perfil pelo id")
    @Transactional
    public void findByPerfilId() throws Exception {

        Perfil perfilAdmin = entityManager.find(Perfil.class, PerfilEnum.ADMIN.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/perfils/{id}", perfilAdmin.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("deve retorna código 404 ao buscar perfil pelo id e não encontrar o recurso")
    @Transactional
    public void findByPerfilIdNotExist() throws Exception {

        Long idInexistente = 9999L;
        mockMvc.perform(MockMvcRequestBuilders.get("/perfils/{id}", idInexistente))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
