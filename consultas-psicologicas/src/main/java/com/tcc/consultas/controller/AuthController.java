package com.tcc.consultas.controller;

import com.tcc.consultas.model.UsuarioAdmin;
import com.tcc.consultas.repository.UsuarioAdminRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(
        origins = {
                "http://localhost:5500","http://127.0.0.1:5500",
                "http://localhost:5501","http://127.0.0.1:5501"
        },
        allowCredentials = "true"
)
public class AuthController {

    private final UsuarioAdminRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioAdminRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Para testar rapidamente se o Basic Auth está ok
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails user) {
        if (user == null) return ResponseEntity.status(401).body(Map.of("message","Não autenticado"));
        return ResponseEntity.ok(Map.of("email", user.getUsername(), "roles", user.getAuthorities()));
    }

    @PostMapping(
            value = "/change-password",
            consumes = {"application/json", "application/x-www-form-urlencoded"}
    )
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody(required = false) Map<String, Object> json,
            @RequestParam(required = false) Map<String, String> form
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Não autenticado"));
        }

        // 1) Captura credenciais a partir de JSON OU FORM
        String senhaAtual = null;
        String senhaNova  = null;

        if (json != null) {
            Object sa = json.getOrDefault("senhaAtual", json.get("oldPassword"));
            Object sn = json.getOrDefault("senhaNova",  json.get("newPassword"));
            senhaAtual = sa == null ? null : sa.toString();
            senhaNova  = sn == null ? null : sn.toString();
        } else if (form != null) {
            senhaAtual = form.getOrDefault("senhaAtual", form.get("oldPassword"));
            senhaNova  = form.getOrDefault("senhaNova",  form.get("newPassword"));
        }

        if (senhaAtual == null || senhaNova == null ||
                senhaAtual.isBlank() || senhaNova.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Body inválido. Envie {senhaAtual, senhaNova} ou {oldPassword, newPassword} via JSON ou form."
            ));
        }

        // 2) Busca usuário autenticado
        String email = user.getUsername();
        UsuarioAdmin u = usuarioRepository.findByEmail(email).orElse(null);
        if (u == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuário não encontrado."));
        }

        // 3) Confere senha atual
        if (!passwordEncoder.matches(senhaAtual, u.getSenhaHash())) {
            return ResponseEntity.status(401).body(Map.of("message", "Senha atual inválida."));
        }

        // 4) Atualiza
        u.setSenhaHash(passwordEncoder.encode(senhaNova));
        usuarioRepository.save(u);

        return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso."));
    }

}
