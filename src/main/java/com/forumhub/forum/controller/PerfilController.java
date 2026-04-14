package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Perfil;
import com.forumhub.forum.dto.PerfilDTO;
import com.forumhub.forum.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/perfils")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @GetMapping
    public ResponseEntity<List<PerfilDTO>> findAll() {
       List <PerfilDTO> perfils = perfilService.findAll();
       return ResponseEntity.ok().body(perfils);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PerfilDTO> findById(@PathVariable Long id){
        PerfilDTO perfilDTO = perfilService.findById(id);
        return ResponseEntity.ok().body(perfilDTO);
    }


    @DeleteMapping("/usuarios/{usuarioId}/perfis/{perfilId}")
    public ResponseEntity<Void> deletePerfil(@PathVariable Long usuarioId, @PathVariable Long perfilId){
       perfilService.removePerfilDoUsuario(usuarioId, perfilId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/perfis/{perfilId}/usuarios/{usuarioId}")
    public ResponseEntity<Void> updatePerfil(@PathVariable Long perfilId, @PathVariable Long usuarioId){
        perfilService.update(perfilId,usuarioId);
        return  ResponseEntity.noContent().build();
    }

}
