package com.tcc.consultas.service;

import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final NotificacaoEmailService notificacaoEmailService;

    public ConsultaService(ConsultaRepository consultaRepository, NotificacaoEmailService notificacaoEmailService) {
        this.consultaRepository = consultaRepository;
        this.notificacaoEmailService = notificacaoEmailService;
    }

    public Consulta salvar(Consulta consulta) {
        Consulta salva = consultaRepository.save(consulta);
        notificacaoEmailService.enviarAgendamento(salva);
        return salva;
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    public Consulta atualizar(Long id, Consulta consulta) {
        Consulta existente = buscarPorId(id);
        existente.setDataHora(consulta.getDataHora());
        existente.setPaciente(consulta.getPaciente());
        existente.setPsicologo(consulta.getPsicologo());
        existente.setStatus(consulta.getStatus());
        existente.setObservacoes(consulta.getObservacoes());
        Consulta atualizada = consultaRepository.save(existente);

        switch (atualizada.getStatus()) {
            case CONFIRMADA -> notificacaoEmailService.enviarConfirmacao(atualizada);
            case CANCELADA -> notificacaoEmailService.enviarCancelamento(atualizada);
            case REMARCADA -> notificacaoEmailService.enviarRemarcacao(atualizada);
        }
        return atualizada;
    }

    public void deletar(Long id) {
        consultaRepository.deleteById(id);
    }
}
