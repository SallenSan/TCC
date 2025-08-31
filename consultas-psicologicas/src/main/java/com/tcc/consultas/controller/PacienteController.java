package com.tcc.consultas.controller;

import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.service.PacienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public Paciente criar(@RequestBody Paciente paciente) {
        return pacienteService.salvar(paciente);
    }

    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Paciente buscar(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Paciente atualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        return pacienteService.atualizar(id, paciente);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
    }
}
