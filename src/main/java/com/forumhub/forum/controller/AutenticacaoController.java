package com.forumhub.forum.controller;

import com.forumhub.forum.domain.Usuario;
import com.forumhub.forum.dto.DadosTokenJWT;
import com.forumhub.forum.dto.UsuarioLoginDTO;
import com.forumhub.forum.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

@PostMapping
    public ResponseEntity autenticar(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuarioLoginDTO.email(), usuarioLoginDTO.senha());
    Authentication authentication = manager.authenticate(token);
    var tokenJWT = tokenService.generarToken((Usuario) authentication.getPrincipal());

    return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
}
}
