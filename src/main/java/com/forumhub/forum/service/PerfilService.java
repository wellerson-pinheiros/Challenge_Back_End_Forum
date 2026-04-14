package com.forumhub.forum.service;

import com.forumhub.forum.domain.Perfil;
import com.forumhub.forum.domain.Usuario;
import com.forumhub.forum.dto.PerfilDTO;
import com.forumhub.forum.excecoes.ResourceNotFoundException;
import com.forumhub.forum.repositorio.PerfilRepository;
import com.forumhub.forum.repositorio.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<PerfilDTO> findAll(){
       List <Perfil> perfis = perfilRepository.findAll();
       return perfis.stream().map(PerfilDTO::new).toList();
    }

    public PerfilDTO findById(Long id){
        Perfil perfil = perfilRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Perfil não encontrado"));
        return new PerfilDTO(perfil);
    }

    @Transactional
    public void save (PerfilDTO perfilDTO, Long idusuario){
        Usuario usuario = usuarioRepository.findById(idusuario).orElseThrow(()-> new ResourceNotFoundException("Usuario não encontrado"));
        Perfil perfil = new Perfil();
        perfil.setNome(perfilDTO.nome());
        perfil.addUsuario(usuario);
        usuario.addPerfil(perfil);
        perfilRepository.save(perfil);
    }

    @Transactional
    public void update(Long perfilId, Long idusuario){
        Usuario usuario = usuarioRepository.findById(idusuario).orElseThrow(()-> new ResourceNotFoundException("Usuario não encontrado"));
        Perfil perfil = perfilRepository.findById(perfilId).orElseThrow(()-> new ResourceNotFoundException("Perfil não encontrado"));
        usuario.addPerfil(perfil);
        perfil.getUsuarios().add(usuario);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void removePerfilDoUsuario(Long usuarioId, Long perfilId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        usuario.getPerfis().remove(perfil);
        perfil.getUsuarios().remove(usuario);

        usuarioRepository.save(usuario);
    }
}
