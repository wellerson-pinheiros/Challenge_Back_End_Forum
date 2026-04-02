package com.forumhub.forum.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "TB_RESPOSTAS")
public class Respostas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 300)
    private String mensagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private  Curso curso;

    @CreationTimestamp
    private Instant dataCriacao;

    //private Usuario autor;

    @Column(nullable = true)
    private Boolean solucao;

    public Respostas(){}

    public Respostas(long id, String mensagem, Topico topico, Curso curso, Instant dataCriacao, Boolean solucao) {
        this.id = id;
        this.mensagem = mensagem;
        this.topico = topico;
        this.curso = curso;
        this.dataCriacao = dataCriacao;
        this.solucao = solucao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getSolucao() {
        return solucao;
    }

    public void setSolucao(Boolean solucao) {
        this.solucao = solucao;
    }

    public Instant getDatacriacao() {
        return dataCriacao;
    }

    public void setDatacriacao(Instant datacriacao) {
        this.dataCriacao = datacriacao;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
