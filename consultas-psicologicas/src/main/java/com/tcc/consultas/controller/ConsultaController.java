package com.tcc.consultas.controller;

import com.tcc.consultas.dto.ConsultaRequestDTO;
import com.tcc.consultas.dto.ConsultaResponseDTO;
import com.tcc.consultas.dto.ConsultaUpdateDTO;
import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.service.ConsultaService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173"}) // ajuste se o front usar outra origem
@RestController
@RequestMapping("/consultas")
@Validated
public class ConsultaController {

    private static final ZoneId ZONE_SP = ZoneId.of("America/Sao_Paulo");

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    // POST /consultas  (usa ConsultaRequestDTO com OffsetDateTime)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponseDTO criar(@RequestBody ConsultaRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payload da consulta é obrigatório.");
        }
        if (dto.pacienteId() == null) {
            throw new IllegalArgumentException("pacienteId é obrigatório.");
        }
        if (dto.psicologoId() == null) {
            throw new IllegalArgumentException("psicologoId é obrigatório.");
        }
        if (dto.dataHora() == null) {
            throw new IllegalArgumentException("dataHora é obrigatório (ISO-8601 com offset, ex: 2025-09-12T10:30:00-03:00).");
        }

        // Monta uma Consulta mínima com somente os IDs (o service recarrega do banco)
        Consulta consulta = new Consulta();

        Paciente p = new Paciente();
        p.setId(dto.pacienteId());
        consulta.setPaciente(p);

        Psicologo psi = new Psicologo();
        psi.setId(dto.psicologoId());
        consulta.setPsicologo(psi);

        // Converte o OffsetDateTime recebido para horário de São Paulo e remove o offset (LocalDateTime)
        var ldtSaoPaulo = dto.dataHora().atZoneSameInstant(ZONE_SP).toLocalDateTime();
        consulta.setDataHora(ldtSaoPaulo);

        consulta.setObservacoes(dto.observacoes());

        Consulta salva = consultaService.salvar(consulta);
        return toResponse(salva);
    }

    // GET /consultas
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConsultaResponseDTO> listar() {
        return consultaService.listarTodas()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET /consultas/{id}
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponseDTO buscar(@PathVariable Long id) {
        return toResponse(consultaService.buscarPorId(id));
    }

    // PUT /consultas/{id} (usa ConsultaUpdateDTO com OffsetDateTime opcional)
   /* @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponseDTO atualizar(@PathVariable Long id, @RequestBody ConsultaUpdateDTO dto) {
        // A lógica de conversão do horário (se vier) fica no service.atualizar(id, dto)
        // para manter a regra em um único lugar.
        var atualizada = consultaService.atualizar(id, dto);
        return toResponse(atualizada);
    } */

    // DELETE /consultas/{id}
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        consultaService.deletar(id);
    }

    // --- mapper local ---
    private ConsultaResponseDTO toResponse(Consulta c) {
        String pacienteNome  = (c.getPaciente()  != null) ? c.getPaciente().getNome()  : null;
        String psicologoNome = (c.getPsicologo() != null) ? c.getPsicologo().getNome() : null;

        String statusStr = (c.getStatus() != null)
                ? (c.getStatus() instanceof Enum<?> ? ((Enum<?>) c.getStatus()).name() : String.valueOf(c.getStatus()))
                : null;

        return new ConsultaResponseDTO(
                c.getId(),
                pacienteNome,
                psicologoNome,
                c.getDataHora(),
                statusStr,
                c.getObservacoes()
        );
    }
}
