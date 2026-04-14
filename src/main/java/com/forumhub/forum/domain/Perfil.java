package com.forumhub.forum.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forumhub.forum.enums.PerfilEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = ("TB_PERFIL"))
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column( length = 100, unique = true )
    @Enumerated(EnumType.STRING)
    private PerfilEnum nome;

    @JsonIgnore
    @JsonBackReference
    @ManyToMany(mappedBy = "perfis")
    private Set<Usuario> usuarios = new HashSet<Usuario>();

    public Perfil() {}

    public Perfil(Set<Usuario> usuarios, Long id, PerfilEnum nome) {
        this.usuarios = usuarios;
        this.id = id;
        this.nome = nome;
    }

    public Perfil(String name) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(PerfilEnum nome) {
        this.nome = nome;
    }


    public Set<Usuario> getUsuarios() {
        return usuarios;
    }
    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
    }

    public PerfilEnum getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Perfil perfil)) return false;
        return Objects.equals(id, perfil.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
