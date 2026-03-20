package com.forumhub.forum.controller;

import com.forumhub.forum.dto.CategoriaSimplesDTO;
import com.forumhub.forum.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity <List<CategoriaSimplesDTO>> findAll(){
        List<CategoriaSimplesDTO> categorias = categoriaService.findAll();
        return ResponseEntity.ok().body(categorias);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity <CategoriaSimplesDTO> findById(@PathVariable Long id){
        CategoriaSimplesDTO categoria = categoriaService.findById(id);
        return ResponseEntity.ok().body(categoria);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CategoriaSimplesDTO categoriaSimplesDTO){
        categoriaService.save(categoriaSimplesDTO);
        URI uri = URI.create("/categorias/");
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }


}
