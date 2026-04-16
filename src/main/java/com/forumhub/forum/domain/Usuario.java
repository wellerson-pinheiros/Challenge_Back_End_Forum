package com.forumhub.forum.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.forumhub.forum.dto.UsuarioCreatDTO;
import com.forumhub.forum.dto.UsuarioDTO;
import com.forumhub.forum.enums.PerfilEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "TB_USUARIOS")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estár vazio e tem que ter mais que três letras")
    String nome;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    String email;

    @NotNull
    @Column(nullable = false)
    String senha;


    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "usuario_perfis",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfis =  new HashSet<Perfil>();

    @JsonBackReference
    @OneToMany(mappedBy = "usuario")
    private Set<Topico> topicos = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "autor")
    private List<Respostas> resposta = new ArrayList<>();

    public Usuario() {}

    public Usuario(Long id, Set<Perfil> perfis, String senha, String email, String nome) {
        this.id = id;
        this.perfis = perfis;
        this.senha = senha;
        this.email = email;
        this.nome = nome;
    }

    public Usuario(UsuarioCreatDTO usuarioDTO) {
        this.email = usuarioDTO.email();
        this.nome = usuarioDTO.nome();
        this.senha = usuarioDTO.senha();
    }


    public Long getId() {
        return id;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil);
        perfil.getUsuarios().add(this); // atualiza o lado inverso
    }

    public void removePerfil(Perfil perfil) {
        this.perfis.remove(perfil);
        perfil.getUsuarios().remove(this); // atualiza o lado inverso
    }

    public void addResposta(Respostas resposta) {
        this.resposta.add(resposta);   // adiciona no Set do usuário
               // atualiza o lado dono da relação
    }

    public void removeResposta(Respostas resposta) {
        this.resposta.remove(resposta);
               // desvincula do usuário
    }

    public void addTopico(Topico topico) {
        this.topicos.add(topico);   // adiciona no Set do usuário
        // atualiza o lado dono da relação
    }

    public void removeTopico(Topico topico) {
        this.topicos.remove(topico);
        // desvincula do usuário
    }

    public Set<Topico> getTopicos() {
        return topicos;
    }

    public List<Respostas> getResposta() {
        return resposta;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.getNome().name()))
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
