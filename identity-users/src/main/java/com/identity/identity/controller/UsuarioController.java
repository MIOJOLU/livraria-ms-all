package com.identity.identity.controller;

import com.identity.identity.repository.UsuarioEntity;
import com.identity.identity.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioEntity> salvar(@RequestBody UsuarioEntity usuario) {
        return ResponseEntity.ok(usuarioService.criarUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getUsuarioById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuario(id));
    }

}
