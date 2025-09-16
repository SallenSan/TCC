package com.tcc.consultas.controller;

import com.tcc.consultas.dto.ConsultaRequestDTO;
import com.tcc.consultas.dto.ConsultaResponseDTO;
import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.service.ConsultaService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/consultas")
@Validated
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponseDTO criar(@RequestBody ConsultaRequestDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Payload da consulta é obrigatório.");
        if (dto.pacienteId() == null) throw new IllegalArgumentException("pacienteId é obrigatório.");
        if (dto.psicologoId() == null) throw new IllegalArgumentException("psicologoId é obrigatório.");
        if (dto.dataHora() == null || dto.dataHora().isBlank())
            throw new IllegalArgumentException("dataHora é obrigatório (ex.: 2025-09-15T15:25 ou ISO com offset).");

        Consulta consulta = new Consulta();

        Paciente p = new Paciente();
        p.setId(dto.pacienteId());
        consulta.setPaciente(p);

        Psicologo psi = new Psicologo();
        psi.setId(dto.psicologoId());
        consulta.setPsicologo(psi);

        // ENTIDADE COM OffsetDateTime:
        consulta.setDataHora(parseToOffsetDateTime(dto.dataHora()).toLocalDateTime());


        consulta.setObservacoes(dto.observacoes());

        Consulta salva = consultaService.salvar(consulta);
        return toResponse(salva);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConsultaResponseDTO> listar() {
        return consultaService.listarTodas().stream().map(this::toResponse).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponseDTO buscar(@PathVariable Long id) {
        return toResponse(consultaService.buscarPorId(id));
    }

   /* @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsultaResponseDTO atualizar(@PathVariable Long id, @RequestBody ConsultaUpdateDTO dto) {
        var atualizada = consultaService.atualizar(id, dto);
        return toResponse(atualizada);
    } */

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        consultaService.deletar(id);
    }

    // --- Mappers/Helpers ---

    private ConsultaResponseDTO toResponse(Consulta c) {
        String pacienteNome = c.getPaciente() != null ? c.getPaciente().getNome() : null;
        String psicologoNome = c.getPsicologo() != null ? c.getPsicologo().getNome() : null;
        return new ConsultaResponseDTO(
                c.getId(),
                pacienteNome,
                psicologoNome,
                c.getDataHora(), // se for OffsetDateTime no ResponseDTO, mantenha. Se o ResponseDTO for String, formate aqui.
                c.getStatus() != null ? c.getStatus().name() : null,
                c.getObservacoes()
        );
    }

    /**
     * Aceita:
     *  - ISO com offset (ex.: 2025-09-15T15:25:00-03:00 ou ...Z)
     *  - "yyyy-MM-dd'T'HH:mm" (sem offset) -> assume ZoneId.systemDefault()
     */
    public static OffsetDateTime parseToOffsetDateTime(String raw) {
        if (raw == null || raw.isBlank())
            throw new IllegalArgumentException("dataHora é obrigatório.");

        // 1) Tenta ISO com offset
        try {
            return OffsetDateTime.parse(raw, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception ignore) {}

        // 2) Tenta sem offset (datetime-local)
        try {
            var ldt = LocalDateTime.parse(raw, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            return ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime();
        } catch (Exception ignore) {}

        // 3) Tenta ainda variante com segundos (opcional) sem offset
        try {
            var ldt = LocalDateTime.parse(raw, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            return ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime();
        } catch (Exception ignore) {}

        throw new IllegalArgumentException("Formato inválido para dataHora. Use 'yyyy-MM-ddTHH:mm' ou ISO com offset (ex.: 2025-09-15T15:25:00-03:00).");
    }
}