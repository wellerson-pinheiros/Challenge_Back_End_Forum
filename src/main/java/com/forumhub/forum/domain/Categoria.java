package com.forumhub.forum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TB_CATEGORIAS")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name of this category cannot be empty.")
    @Column(unique = true)
    private String nome;

    @Column(length = 300)
    private String descricao;


    private boolean ativo = true;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Curso> cursos = new HashSet<>();

    public Categoria() {
    }

    public Categoria(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Categoria(
            @NotBlank(message = "O nome não pode ser nulo")
            String nome,
            @Size(max = 300, message = "Descrição deve ter no máximo 300 caracteres" )
            String descricao) {

        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void addCursos(Curso curso) {
        cursos.add(curso);
        curso.setCategoria(this);
    }

    public void removeCurso(Curso curso) {
        cursos.remove(curso);
        curso.setCategoria(null);
    }

    public Set<Curso> getCursos() {
        return cursos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Categoria categoria)) return false;
        return Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
