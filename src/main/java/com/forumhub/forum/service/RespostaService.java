package com.forumhub.forum.service;

import com.forumhub.forum.domain.Respostas;
import com.forumhub.forum.domain.Topico;
import com.forumhub.forum.dto.AtualizarMensagemDTO;
import com.forumhub.forum.dto.RespostasDTO;
import com.forumhub.forum.excecoes.ResourceNotFoundException;
import com.forumhub.forum.repositorio.RespostaRepository;
import com.forumhub.forum.repositorio.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    public List<RespostasDTO> findAll() {
        List <Respostas> respostas = respostaRepository.findAll();
        return respostas.stream().map(RespostasDTO::new).toList();
    }

    public RespostasDTO findById(Long id) {
        Respostas respostas = respostaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resposta não encontrada"));
        return new RespostasDTO(respostas);
    }

    public List<RespostasDTO> findByTopico(Long topicoId) {
        Topico topico = topicoRepository.findById(topicoId).orElseThrow(()-> new ResourceNotFoundException("Tópico não encontrado"));
        return topico.getRespostas().stream().map(RespostasDTO::new).toList();
    }

    @Transactional
    public void save(RespostasDTO respostasDTO) {

        // primeiro verificar se a resposta existe
       Topico topico = topicoRepository.findById(respostasDTO.topico()).orElseThrow(()-> new ResourceNotFoundException("Tópico não encontrado"));

       Respostas respostas = new Respostas();
       respostas.setMensagem(respostasDTO.mensagem());
       respostas.setDatacriacao(Instant.now());
       respostas.setSolucao(false);
       respostas.setTopico(topico);
       respostaRepository.save(respostas);
    }

    @Transactional
    public void update(AtualizarMensagemDTO respostasDTO) {
        Respostas respostas = respostaRepository.findById(respostasDTO.id()).orElseThrow(()-> new ResourceNotFoundException("Resposta não encontrada"));
        respostas.setMensagem(respostasDTO.mensagem());
        respostas.setDatacriacao(Instant.now());
        respostas.setSolucao(respostasDTO.solucao());
        respostaRepository.save(respostas);
    }

    @Transactional
    public void delete(Long id) {
        Respostas respostas = respostaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resposta não encontrada"));
        respostaRepository.delete(respostas);
    }
}
