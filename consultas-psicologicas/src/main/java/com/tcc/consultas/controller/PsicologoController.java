package com.tcc.consultas.controller;

import com.tcc.consultas.dto.PsicologoResponseDTO;
import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.service.PsicologoService;
import org.springframework.http.MediaType;
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

    // CREATE — mantém retorno como entidade (se preferir DTO aqui também, me diga)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Psicologo criar(@RequestBody Psicologo psicologo) {
        return psicologoService.salvar(psicologo);
    }

    // READ ALL — retorna DTO já com e-mail resolvido
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PsicologoResponseDTO> listar() {
        return psicologoService.listarTodos()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // READ ONE — retorna DTO já com e-mail resolvido
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PsicologoResponseDTO buscar(@PathVariable Long id) {
        return toDTO(psicologoService.buscarPorId(id));
    }

    // UPDATE — mantém retorno como entidade (pode trocar para DTO se quiser padronizar)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Psicologo atualizar(@PathVariable Long id, @RequestBody Psicologo psicologo) {
        return psicologoService.atualizar(id, psicologo);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        psicologoService.deletar(id);
    }

    // --- mapper local (preenche email a partir do Usuario se necessário) ---
    private PsicologoResponseDTO toDTO(Psicologo p) {
        String email = (p.getUsuario() != null && p.getUsuario().getEmail() != null)
                ? p.getUsuario().getEmail()
                : p.getEmail();

        return new PsicologoResponseDTO(
                p.getId(),
                p.getNome(),
                email,
                p.getTelefone(),
                p.getCrp(),
                p.getEspecialidade()
        );
    }
}
