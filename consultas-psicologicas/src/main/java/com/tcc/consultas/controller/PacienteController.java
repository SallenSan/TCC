package com.tcc.consultas.controller;

import com.tcc.consultas.dto.PacienteResponseDTO;
import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.service.PacienteService;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Paciente criar(@RequestBody Paciente paciente) {
        return pacienteService.salvar(paciente);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PacienteResponseDTO> listar() {
        return pacienteService.listarTodos()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PacienteResponseDTO buscar(@PathVariable Long id) {
        return toDTO(pacienteService.buscarPorId(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Paciente atualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        return pacienteService.atualizar(id, paciente);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
    }

    private PacienteResponseDTO toDTO(Paciente p) {
        // SE houver Usuario vinculado, usa o email de Usuario; senão, usa o próprio campo do Paciente
        String email = (p.getUsuario() != null && p.getUsuario().getEmail() != null)
                ? p.getUsuario().getEmail()
                : p.getEmail();

        return new PacienteResponseDTO(
                p.getId(),
                p.getNome(),
                email,
                p.getTelefone()
        );
    }
}
