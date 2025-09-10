package com.tcc.consultas.controller;

import com.tcc.consultas.model.UsuarioAdmin;
import com.tcc.consultas.service.UsuarioAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario-admin")
@CrossOrigin(origins = {
        "http://localhost:5500", "http://localhost:5501",
        "http://127.0.0.1:5500", "http://127.0.0.1:5501"
})
public class UsuarioAdminController {

    @Autowired
    private UsuarioAdminService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioAdmin> criar(@Valid @RequestBody UsuarioAdmin usuario) {
        UsuarioAdmin salvo = usuarioService.salvar(usuario);
        return ResponseEntity
                .created(URI.create("/usuario-admin/" + salvo.getId()))
                .body(salvo);
    }

    @GetMapping
    public List<UsuarioAdmin> listar() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioAdmin> buscar(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint util para a index.html validar admin por e-mail
    // GET /usuario-admin/by-email?email=admin@system.com
    @GetMapping("/by-email")
    public ResponseEntity<UsuarioAdmin> buscarPorEmail(@RequestParam String email) {
        Optional<UsuarioAdmin> opt = usuarioService.buscarPorEmail(email);
        return opt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioAdmin> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioAdmin usuario) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
