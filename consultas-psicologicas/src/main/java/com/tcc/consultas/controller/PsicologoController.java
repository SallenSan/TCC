package com.tcc.consultas.controller;

import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.repository.PsicologoRepository;
import com.tcc.consultas.service.PsicologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/psicologos")
public class PsicologoController {

    @Autowired
    private PsicologoService psicologoService;

    @Autowired
    private PsicologoRepository psicologoRepository;

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
    public ResponseEntity<?> salvar(@RequestBody Psicologo psicologo) {
        try {
            // Verificações básicas (pode ser melhorado com DTO + Bean Validation futuramente)
            if (psicologo.getNome() == null || psicologo.getEmail() == null || psicologo.getSenha() == null ||
                    psicologo.getTelefone() == null || psicologo.getEspecialidade() == null || psicologo.getCrp() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Todos os campos são obrigatórios.");
            }

            // Verifica se já existe um psicólogo com o mesmo CRP
            if (psicologoRepository.findAll().stream().anyMatch(p -> p.getCrp().equals(psicologo.getCrp()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Já existe um psicólogo com este CRP.");
            }

            Psicologo salvo = psicologoRepository.save(psicologo);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar psicólogo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (psicologoRepository.existsById(id)) {
            psicologoService.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPsicologo(@PathVariable Long id, @RequestBody Psicologo psicologoAtualizado) {
        Optional<Psicologo> existente = psicologoService.buscarPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            psicologoAtualizado.setId(id);
            Psicologo atualizado = psicologoService.atualizarPsicologo(id, psicologoAtualizado);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar psicólogo: " + e.getMessage());
        }
    }
}
