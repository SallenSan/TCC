package com.tcc.consultas.service;

import com.tcc.consultas.model.Consulta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") // pega o e-mail configurado no application.properties
    private String remetente;

    public NotificacaoEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarAgendamento(Consulta consulta) {
        enviar("Agendamento realizado", consulta);
    }

    public void enviarConfirmacao(Consulta consulta) {
        enviar("Consulta confirmada", consulta);
    }

    public void enviarCancelamento(Consulta consulta) {
        enviar("Consulta cancelada", consulta);
    }

    public void enviarRemarcacao(Consulta consulta) {
        enviar("Consulta remarcada", consulta);
    }

    private void enviar(String assunto, Consulta consulta) {
        String emailPaciente = consulta.getPaciente().getUsuario().getEmail();
        String emailPsicologo = consulta.getPsicologo().getUsuario().getEmail();

        SimpleMailMessage msgPaciente = new SimpleMailMessage();
        msgPaciente.setSubject(assunto);
        msgPaciente.setText("Olá " + consulta.getPaciente().getNome() +
                ", sua consulta está marcada para " + consulta.getDataHora() +
                " - Status: " + consulta.getStatus());
        msgPaciente.setTo(emailPaciente);
        msgPaciente.setFrom(remetente); // remetente
        mailSender.send(msgPaciente);

        SimpleMailMessage msgPsicologo = new SimpleMailMessage();
        msgPsicologo.setSubject(assunto);
        msgPsicologo.setText("Consulta com paciente " + consulta.getPaciente().getNome() +
                " em " + consulta.getDataHora() +
                " - Status: " + consulta.getStatus());
        msgPsicologo.setTo(emailPsicologo);
        msgPsicologo.setFrom(remetente); // remetente
        mailSender.send(msgPsicologo);
    }
}
