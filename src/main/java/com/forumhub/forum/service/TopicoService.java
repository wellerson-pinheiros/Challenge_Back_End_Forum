package com.forumhub.forum.service;

import com.forumhub.forum.domain.Curso;
import com.forumhub.forum.domain.Topico;
import com.forumhub.forum.domain.Usuario;
import com.forumhub.forum.dto.TopicoDTO;
import com.forumhub.forum.enums.StatusTopico;
import com.forumhub.forum.excecoes.ResourceNotFoundException;
import com.forumhub.forum.repositorio.CursoRepository;
import com.forumhub.forum.repositorio.TopicoRepository;
import com.forumhub.forum.repositorio.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<TopicoDTO> findAllTopicos() {
        List<Topico> topicos =  topicoRepository.findAll();
         return topicos.stream().map(TopicoDTO::new).toList();
    }

    public TopicoDTO findById(Long id) {
        Topico topico = topicoRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Tópico não encotrado " + id));
        return new TopicoDTO(topico);
    }

    public List <TopicoDTO> findByCurso(String nomeCurso) {
       List<Topico> topico = topicoRepository.findByCurso_NomeContainingIgnoreCase(nomeCurso);
        return topico.stream().map(TopicoDTO::new).toList();
    }

    @Transactional
    public void save(TopicoDTO topicoDTO) {
        // primeiro verificar se o curso existe
        Curso curso = cursoRepository.findById(topicoDTO.cursoId()).orElseThrow(()-> new ResourceNotFoundException("Curso não encontrado"));
        Usuario usuario = usuarioRepository.findById(topicoDTO.usuarioId()).orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado"));
        Topico topico = new Topico();
        topico.setTitulo(topicoDTO.titulo());
        topico.setMensagem(topicoDTO.mensagem());
        topico.setDataCriacao(Instant.now());
        topico.setStatus(StatusTopico.valueOf("Publicado"));
        topico.setCurso(curso);
        topico.setUsuario(usuario);

        topicoRepository.save(topico);
    }

    @Transactional
    public void update(TopicoDTO topicoDTO) {
        Topico topico = topicoRepository.findById(topicoDTO.id()).orElseThrow(() -> new ResourceNotFoundException("Topico não encontrado"));
        topico.setTitulo(topicoDTO.titulo());
        topico.setMensagem(topicoDTO.mensagem());
        topico.setDataCriacao(Instant.now());
        topicoRepository.save(topico);
    }

    @Transactional
    public void delete(Long id) {
        Topico topico = topicoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Tópico não encontrado"));
        topicoRepository.deleteById(id);
    }

}
