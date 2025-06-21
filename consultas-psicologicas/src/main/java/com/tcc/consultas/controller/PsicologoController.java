package com.tcc.consultas.controller;

import com.tcc.consultas.dto.PsicologoDTO;
import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.service.PsicologoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/psicologos")
public class PsicologoController {

    @Autowired
    private PsicologoService psicologoService;
    private PsicologoDTO psicologoDTO;

    @GetMapping
    public List<Psicologo> listarTodos() {
        return psicologoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Psicologo> buscarPorId(@PathVariable Long id) {
        Optional<Psicologo> psicologo = psicologoService.buscarPorId(id);
        return psicologo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid PsicologoDTO dto) {
        try {
            Psicologo salvo = psicologoService.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar psicólogo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (psicologoService.existePorId(id)) {
            psicologoService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid PsicologoDTO dto) {
        try {
            Psicologo atualizado = psicologoService.atualizar(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar psicólogo: " + e.getMessage());
        }
    }
}
