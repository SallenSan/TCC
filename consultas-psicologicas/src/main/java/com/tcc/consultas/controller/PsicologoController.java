package com.tcc.consultas.controller;

import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.service.PsicologoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/psicologos")
public class PsicologoController {
    private final PsicologoService psicologoService;

    public PsicologoController(PsicologoService psicologoService) {
        this.psicologoService = psicologoService;
    }

    @PostMapping
    public Psicologo criar(@RequestBody Psicologo psicologo) {
        return psicologoService.salvar(psicologo);
    }

    @GetMapping
    public List<Psicologo> listar() {
        return psicologoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Psicologo buscar(@PathVariable Long id) {
        return psicologoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Psicologo atualizar(@PathVariable Long id, @RequestBody Psicologo psicologo) {
        return psicologoService.atualizar(id, psicologo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        psicologoService.deletar(id);
    }
}
