package com.tcc.consultas.controller;

import com.tcc.consultas.model.UsuarioAdmin;
import com.tcc.consultas.service.UsuarioAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario_admin")
public class UsuarioAdminController {

    @Autowired
    private UsuarioAdminService usuarioService;

    @PostMapping
    public UsuarioAdmin criar(@RequestBody UsuarioAdmin usuario) {
        return usuarioService.salvar(usuario);
    }

    @GetMapping
    public List<UsuarioAdmin> listar() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAdmin> buscar(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioAdmin> atualizar(@PathVariable Long id, @RequestBody UsuarioAdmin usuario) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
