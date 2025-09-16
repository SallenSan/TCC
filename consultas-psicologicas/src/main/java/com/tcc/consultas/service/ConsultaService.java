package com.tcc.consultas.service;

import com.tcc.consultas.controller.ConsultaController; // parse helper
import com.tcc.consultas.dto.ConsultaUpdateDTO;
import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteService pacienteService;
    private final PsicologoService psicologoService;

    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteService pacienteService,
                           PsicologoService psicologoService) {
        this.consultaRepository = consultaRepository;
        this.pacienteService = pacienteService;
        this.psicologoService = psicologoService;
    }

    @Transactional
    public Consulta salvar(Consulta c) {
        // Garantir que Paciente/Psicólogo existem (evita detached/transient)
        c.setPaciente(pacienteService.buscarPorId(c.getPaciente().getId()));
        c.setPsicologo(psicologoService.buscarPorId(c.getPsicologo().getId()));

        // Se quiser forçar default de status quando vier null do controller:
        if (c.getStatus() == null) {
            c.setStatus(Consulta.StatusConsulta.AGENDADA);
        }

        return consultaRepository.save(c);
    }

    @Transactional(readOnly = true)
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada id=" + id));
    }

    @Transactional
    public Consulta atualizar(Long id, ConsultaUpdateDTO dto) {
        Consulta c = buscarPorId(id);

        // dataHora vem como String (com ou sem offset). Convertemos para LocalDateTime.
        if (dto.dataHora() != null && !dto.dataHora().isBlank()) {
            c.setDataHora(
                    ConsultaController.parseToOffsetDateTime(dto.dataHora())
                            .toLocalDateTime()
            );
        }

        // status: aceitar nomes vindos do front (ex.: PENDENTE → AGENDADA)
        if (dto.status() != null && !dto.status().isBlank()) {
            String raw = dto.status().trim().toUpperCase();

            // normalizações amigáveis
            if ("PENDENTE".equals(raw)) {
                raw = "AGENDADA";
            }

            try {
                c.setStatus(Consulta.StatusConsulta.valueOf(raw));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Status inválido: " + dto.status() +
                                ". Use: AGENDADA, CONFIRMADA, REMARCADA, CANCELADA ou CONCLUIDA."
                );
            }
        }

        if (dto.observacoes() != null) {
            c.setObservacoes(dto.observacoes());
        }

        return consultaRepository.save(c);
    }

    @Transactional
    public void deletar(Long id) {
        consultaRepository.deleteById(id);
    }
}