package com.tcc.consultas.controller;

import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.model.Usuario;
import com.tcc.consultas.service.NotificacaoEmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class EmailTestController {

    private final NotificacaoEmailService notificacaoEmailService;

    public EmailTestController(NotificacaoEmailService notificacaoEmailService) {
        this.notificacaoEmailService = notificacaoEmailService;
    }

    @GetMapping("/teste-email")
    public String enviarEmailTeste() {
        // Criando usuário/paciente fake
        Usuario usuarioPaciente = new Usuario();
        usuarioPaciente.setEmail("consulta.teste.2025@gmail.com"); // altere para um e-mail válido

        Paciente paciente = new Paciente();
        paciente.setNome("Paciente Teste");
        paciente.setUsuario(usuarioPaciente);

        // Criando usuário/psicólogo fake
        Usuario usuarioPsicologo = new Usuario();
        usuarioPsicologo.setEmail("consulta.teste.2025@gmail.com"); // altere para um e-mail válido

        Psicologo psicologo = new Psicologo();
        psicologo.setNome("Psicólogo Teste");
        psicologo.setUsuario(usuarioPsicologo);

        // Criando consulta fake
        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setPsicologo(psicologo);
        consulta.setDataHora(LocalDateTime.now().plusDays(1));
        consulta.setStatus(Consulta.StatusConsulta.valueOf("Agendada"));

        // Disparando notificação
        notificacaoEmailService.enviarAgendamento(consulta);

        return "E-mails de teste enviados!";
    }
}
