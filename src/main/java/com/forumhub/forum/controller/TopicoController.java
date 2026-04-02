package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Topico;
import com.forumhub.forum.dto.TopicoDTO;
import com.forumhub.forum.repositorio.TopicoRepository;
import com.forumhub.forum.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @GetMapping
    public ResponseEntity <List<TopicoDTO>> findAll() {
        List<TopicoDTO> topicos =  topicoService.findAllTopicos();
        return ResponseEntity.ok().body(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity <TopicoDTO> findById(@PathVariable Long id) {
        TopicoDTO topicoDTO = topicoService.findById(id);
        return ResponseEntity.ok().body(topicoDTO);
    }

    @GetMapping("/curso/{nomeCurso}")
    public ResponseEntity <List<TopicoDTO>> findByCurso(@PathVariable String nomeCurso) {
        List<TopicoDTO> topicos = topicoService.findByCurso(nomeCurso);
        return  ResponseEntity.ok().body(topicos);
    }

    @PostMapping
    public ResponseEntity <Void> create(@Valid @RequestBody TopicoDTO topicoDTO) {
        topicoService.save(topicoDTO);
        URI uri = URI.create("/topicos/");
        return ResponseEntity.created(uri).build();
    }

    @PutMapping
    public ResponseEntity update(@Valid @RequestBody TopicoDTO topicoDTO) {
        topicoService.update(topicoDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@Valid @PathVariable Long id) {
        topicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
