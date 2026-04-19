package com.forumhub.forum.service;


import com.forumhub.forum.domain.Perfil;
import com.forumhub.forum.domain.Usuario;

import com.forumhub.forum.dto.UsuarioCreatDTO;
import com.forumhub.forum.dto.UsuarioDTO;
import com.forumhub.forum.enums.PerfilEnum;
import com.forumhub.forum.excecoes.ResourceAlreadyRegistered;
import com.forumhub.forum.excecoes.ResourceNotFoundException;
import com.forumhub.forum.repositorio.PerfilRepository;
import com.forumhub.forum.repositorio.RespostaRepository;
import com.forumhub.forum.repositorio.TopicoRepository;
import com.forumhub.forum.repositorio.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilService perfilService;

    public List<UsuarioDTO> findAll(){
        List <Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(UsuarioDTO::new).toList();
    }

    public UsuarioDTO findById(Long id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado"));
        return new UsuarioDTO(usuario);
    }

    public UsuarioDTO findByEmail(String email){
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado"));
        return new UsuarioDTO(usuario);
    }

    @Transactional
    public void save(Usuario usuario){

        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new ResourceAlreadyRegistered("Usuário já cadastrado com este e-mail");
                });


        usuario.setEmail(usuario.getEmail());
        usuario.setNome(usuario.getEmail());
        String rash = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(rash);

        // 3. Atribuir o perfil USER (ID 2) usando o seu Enum
        // getReferenceById é excelente aqui porque você já sabe o ID e evita um SELECT
        Perfil perfilPadrao = perfilRepository.getReferenceById(PerfilEnum.USER.getId());

        // Adiciona o perfil à lista de perfis do usuário
        usuario.getPerfis().add(perfilPadrao);
        usuarioRepository.save(usuario);
    }


    @Transactional
    public void excluirUsuario(Long idUsuarioReal) {
        var usuarioReal = usuarioRepository.findById(idUsuarioReal).orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado!"));
        Usuario usuarioAnonimo = usuarioRepository.findById(100L)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Usuário Anônimo (ID 100) não cadastrado no banco!"));
        topicoRepository.transferirTopicosParaAnonimo(usuarioReal, usuarioAnonimo);
        respostaRepository.transferirRespostasParaAnonimo(usuarioReal, usuarioAnonimo);
        usuarioRepository.delete(usuarioReal);
    }

    @Transactional
    public void update(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.id()).orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado"));
        usuario.setNome(usuarioDTO.nome());
        usuarioRepository.save(usuario);
    }
}
