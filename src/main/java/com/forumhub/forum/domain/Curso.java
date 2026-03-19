package com.forumhub.forum.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "TB_CURSOS")
public class Curso {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message =  "The name of this curso cannot be empty.")
    private String nome;

    @Column(length = 300)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Curso() {}

    public Curso(Long id, String nome, String descricao) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Curso curso)) return false;
        return Objects.equals(id, curso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
