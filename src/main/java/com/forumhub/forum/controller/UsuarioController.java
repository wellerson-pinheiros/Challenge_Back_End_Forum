package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Usuario;
import com.forumhub.forum.dto.UsuarioCreatDTO;
import com.forumhub.forum.dto.UsuarioDTO;
import com.forumhub.forum.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll(){
       List <UsuarioDTO> usuarios = usuarioService.findAll();
        return  ResponseEntity.ok().body(usuarios);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id){
        UsuarioDTO usuario = usuarioService.findById(id);
        return  ResponseEntity.ok().body(usuario);
    }

    @GetMapping(value ="/email/{email}" )
    public ResponseEntity<UsuarioDTO> findByName(@Valid @PathVariable String email){
        UsuarioDTO usuario = usuarioService.findByEmail(email);
        return  ResponseEntity.ok().body(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioCreatDTO usuarioCreatDTO, UriComponentsBuilder uriComponentsBuilder){
        Usuario usuario = new Usuario(usuarioCreatDTO);
        usuarioService.save(usuario);
        URI uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioDTO(usuario));
    }

    @PutMapping
    public ResponseEntity<Void> update (@Valid @RequestBody UsuarioDTO usuarioDTO, UriComponentsBuilder uriComponentsBuilder){
        usuarioService.update(usuarioDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        usuarioService.excluirUsuario(id);
        return  ResponseEntity.noContent().build();
    }


}
