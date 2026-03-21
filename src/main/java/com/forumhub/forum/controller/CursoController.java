package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Curso;
import com.forumhub.forum.dto.CursoDTO;
import com.forumhub.forum.repositorio.cursoRepository;
import com.forumhub.forum.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value ="/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity <List<CursoDTO>> findAll() {
        List<CursoDTO> cursos = cursoService.findAll();
        return ResponseEntity.ok().body(cursos);
    }

    @GetMapping("/ativos")
    public ResponseEntity <List<CursoDTO>> findAllAtivo() {
        List<CursoDTO> cursos = cursoService.findAllByAtivo();
        return ResponseEntity.ok().body(cursos);
    }

    @GetMapping("/categoria/{nomeCategoria}")
    public ResponseEntity <List<CursoDTO>> findByNomeCategoria(@PathVariable String nomeCategoria) {
        List<CursoDTO> cursos = cursoService.findAllByCategoria(nomeCategoria);
        return ResponseEntity.ok().body(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity <CursoDTO> findById(@PathVariable Long id) {
        CursoDTO cursoDTO = cursoService.findById(id);
        return ResponseEntity.ok().body(cursoDTO);
    }

    @PostMapping
    public ResponseEntity <Void> create(@Valid @RequestBody CursoDTO cursoDTO) {
        cursoService.save(cursoDTO);
        URI uri = URI.create("/cursos/");
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
