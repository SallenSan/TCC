package com.tcc.consultas.service;

import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.repository.ConsultaRepository;
import com.tcc.consultas.repository.PacienteRepository;
import com.tcc.consultas.repository.PsicologoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaService {

    private static final Logger log = LoggerFactory.getLogger(ConsultaService.class);

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final PsicologoRepository psicologoRepository;
    private final NotificacaoEmailService notificacaoEmailService;

    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteRepository pacienteRepository,
                           PsicologoRepository psicologoRepository,
                           NotificacaoEmailService notificacaoEmailService) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.psicologoRepository = psicologoRepository;
        this.notificacaoEmailService = notificacaoEmailService;
    }

    /**
     * Cria/salva uma nova consulta garantindo que Paciente e Psicólogo
     * estejam recarregados do banco (com usuario/email populados).
     */
    @Transactional
    public Consulta salvar(Consulta consulta) {
        validarRequisitosBasicos(consulta);

        // Recarrega entidades do banco para evitar nulls/lazy não inicializados
        Paciente paciente = pacienteRepository.findById(consulta.getPaciente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
        Psicologo psicologo = psicologoRepository.findById(consulta.getPsicologo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado."));

        consulta.setPaciente(paciente);
        consulta.setPsicologo(psicologo);

        Consulta salva = consultaRepository.save(consulta);

        try {
            // Notifica agendamento (falha de e-mail não derruba a transação)
            notificacaoEmailService.enviarAgendamento(salva);
        } catch (RuntimeException ex) {
            log.error("Falha ao enviar notificação de agendamento: {}", ex.getMessage(), ex);
        }

        return salva;
    }

    /**
     * Lista todas as consultas (usado pelo Controller).
     */
    @Transactional(readOnly = true)
    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    /**
     * Busca consulta por ID (usado pelo Controller).
     */
    @Transactional(readOnly = true)
    public Consulta buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id da consulta é obrigatório.");
        }
        return consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada."));
    }

    /**
     * Atualiza consulta existente (usado pelo Controller).
     * - Recarrega Paciente/Psicólogo do banco se IDs vierem no payload.
     * - Mantém o comportamento de notificar sem derrubar transação se e-mail falhar.
     */
    @Transactional
    public Consulta atualizar(Long id, Consulta atualizacao) {
        if (id == null) {
            throw new IllegalArgumentException("Id da consulta é obrigatório.");
        }
        if (atualizacao == null) {
            throw new IllegalArgumentException("Payload da consulta é obrigatório.");
        }

        Consulta existente = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada."));

        // Aplicar campos simples (se vierem no payload)
        if (atualizacao.getDataHora() != null) {
            existente.setDataHora(atualizacao.getDataHora());
        }
        if (atualizacao.getStatus() != null) {
            existente.setStatus(atualizacao.getStatus());
        }
        if (atualizacao.getObservacoes() != null) { // caso exista esse campo
            existente.setObservacoes(atualizacao.getObservacoes());
        }

        // Troca de Paciente (se veio id)
        if (atualizacao.getPaciente() != null && atualizacao.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(atualizacao.getPaciente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
            existente.setPaciente(paciente);
        }

        // Troca de Psicologo (se veio id)
        if (atualizacao.getPsicologo() != null && atualizacao.getPsicologo().getId() != null) {
            Psicologo psicologo = psicologoRepository.findById(atualizacao.getPsicologo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado."));
            existente.setPsicologo(psicologo);
        }

        Consulta salvo = consultaRepository.save(existente);

        // Escolha simples de notificação: se mudou data/hora -> remarcação; se mudou status -> confirmação genérica
        try {
            if (atualizacao.getDataHora() != null) {
                notificacaoEmailService.enviarRemarcacao(salvo);
            } else if (atualizacao.getStatus() != null) {
                notificacaoEmailService.enviarConfirmacao(salvo);
            }
        } catch (RuntimeException ex) {
            log.error("Falha ao enviar notificação na atualização: {}", ex.getMessage(), ex);
        }

        return salvo;
    }

    /**
     * Deleta consulta por ID (usado pelo Controller).
     */
    @Transactional
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id da consulta é obrigatório.");
        }
        if (!consultaRepository.existsById(id)) {
            // idempotente: não explode se já não existir
            return;
        }
        // Opcional: enviar cancelamento antes/após excluir (requer carregar a entidade)
        // Consulta c = consultaRepository.findById(id).orElse(null);
        consultaRepository.deleteById(id);
        // try { if (c != null) notificacaoEmailService.enviarCancelamento(c); } catch (RuntimeException ex) { ... }
    }

    // ---------- Helpers ----------

    private void validarRequisitosBasicos(Consulta consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("Consulta não pode ser nula.");
        }
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
            throw new IllegalArgumentException("Paciente é obrigatório.");
        }
        if (consulta.getPsicologo() == null || consulta.getPsicologo().getId() == null) {
            throw new IllegalArgumentException("Psicólogo é obrigatório.");
        }
    }
}
