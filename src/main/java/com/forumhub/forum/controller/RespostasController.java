package com.forumhub.forum.controller;

import com.forumhub.forum.dto.AtualizarMensagemDTO;
import com.forumhub.forum.dto.RespostasDTO;
import com.forumhub.forum.service.RespostaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/respostas")
public class RespostasController {

    @Autowired
    private RespostaService respostaService;

    @GetMapping
    public ResponseEntity <List<RespostasDTO>> findAll() {
        List<RespostasDTO> respostas =  respostaService.findAll();
        return ResponseEntity.ok().body(respostas);
    }

    @GetMapping("/{id}")
    public ResponseEntity <RespostasDTO> findById(@PathVariable Long id) {
        RespostasDTO respostas =  respostaService.findById(id);
        return  ResponseEntity.ok().body(respostas);
    }

    @GetMapping("/topicos/{topicoId}")
    public ResponseEntity <List<RespostasDTO>> findByTopico(@PathVariable Long topicoId) {
        List<RespostasDTO> respostas = respostaService.findByTopico(topicoId);
        return ResponseEntity.ok().body(respostas);
    }

    @PostMapping
    public ResponseEntity <Void> create(@Valid @RequestBody RespostasDTO respostasDTO) {
        respostaService.save(respostasDTO);
        URI uri = URI.create("/respostas/");
        return ResponseEntity.created(uri).build();
    }

    @PutMapping
    public ResponseEntity <Void> updte(@Valid @RequestBody AtualizarMensagemDTO respostasDTO) {
        respostaService.update(respostasDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(@PathVariable Long id) {
        respostaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
