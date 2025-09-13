package com.tcc.consultas.service;

import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.model.Psicologo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NotificacaoEmailService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoEmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}") // remetente opcional (evita NPE se não estiver setado)
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

    /**
     * Envia e-mail para paciente e/ou psicólogo, se houver e-mail disponível.
     * - Tenta primeiro usuario.email; se nulo, usa paciente/psicologo.email (fallback).
     * - Se nenhum destinatário existir, lança exceção de regra (troque por BusinessException se tiver).
     */
    private void enviar(String assunto, Consulta consulta) {
        if (consulta == null) {
            log.warn("Consulta nula recebida para envio de '{}'", assunto);
            return;
        }

        // Resolve e-mails com fallback
        String emailPaciente = resolverEmailPaciente(consulta.getPaciente());
        String emailPsicologo = resolverEmailPsicologo(consulta.getPsicologo());

        // Se não houver nenhum destinatário, falha controlada (para o ControllerAdvice transformar em 400)
        if (!StringUtils.hasText(emailPaciente) && !StringUtils.hasText(emailPsicologo)) {
            throw new RuntimeException("Nenhum e-mail disponível para notificar (paciente/psicólogo sem e-mail cadastrado/vinculado).");
        }

        // Corpo das mensagens
        String textoPaciente = String.format(
                "Olá %s, sua consulta está marcada para %s - Status: %s",
                safe(consulta.getPaciente() != null ? consulta.getPaciente().getNome() : null),
                String.valueOf(consulta.getDataHora()),
                String.valueOf(consulta.getStatus())
        );

        String textoPsicologo = String.format(
                "Consulta com paciente %s em %s - Status: %s",
                safe(consulta.getPaciente() != null ? consulta.getPaciente().getNome() : null),
                String.valueOf(consulta.getDataHora()),
                String.valueOf(consulta.getStatus())
        );

        // Envia individualmente (se um falhar, tenta o outro)
        int enviados = 0;

        if (StringUtils.hasText(emailPaciente)) {
            enviados += trySend(assunto, textoPaciente, emailPaciente);
        } else {
            log.warn("Paciente sem e-mail. Ignorando envio ao paciente.");
        }

        if (StringUtils.hasText(emailPsicologo)) {
            enviados += trySend(assunto, textoPsicologo, emailPsicologo);
        } else {
            log.warn("Psicólogo sem e-mail. Ignorando envio ao psicólogo.");
        }

        // Se nenhum foi enviado (ex.: SMTP indisponível), propague erro controlado
        if (enviados == 0) {
            throw new RuntimeException("Falha ao enviar e-mails de notificação (nenhuma mensagem foi enviada).");
        }
    }

    /** Tenta enviar e-mail; retorna 1 se enviou, 0 se falhou (com log). */
    private int trySend(String assunto, String corpo, String destinatario) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setSubject(assunto);
            msg.setText(corpo);
            msg.setTo(destinatario);

            if (StringUtils.hasText(remetente)) {
                msg.setFrom(remetente);
            } else {
                log.warn("Remetente (spring.mail.username) não configurado. Enviando sem 'from' explícito.");
            }

            mailSender.send(msg);
            log.info("E-mail '{}' enviado para {}", assunto, destinatario);
            return 1;
        } catch (Exception ex) {
            log.error("Erro ao enviar e-mail '{}' para {}: {}", assunto, destinatario, ex.getMessage(), ex);
            return 0;
        }
    }

    private String resolverEmailPaciente(Paciente paciente) {
        if (paciente == null) return null;
        // 1) usuário vinculado
        try {
            if (paciente.getUsuario() != null && StringUtils.hasText(paciente.getUsuario().getEmail())) {
                return paciente.getUsuario().getEmail();
            }
        } catch (Exception ignored) { /* defensivo */ }
        // 2) e-mail direto do paciente (fallback)
        return StringUtils.hasText(paciente.getEmail()) ? paciente.getEmail() : null;
    }

    private String resolverEmailPsicologo(Psicologo psicologo) {
        if (psicologo == null) return null;
        // 1) usuário vinculado
        try {
            if (psicologo.getUsuario() != null && StringUtils.hasText(psicologo.getUsuario().getEmail())) {
                return psicologo.getUsuario().getEmail();
            }
        } catch (Exception ignored) { /* defensivo */ }
        // 2) e-mail direto do psicólogo (fallback)
        return StringUtils.hasText(psicologo.getEmail()) ? psicologo.getEmail() : null;
    }

    private String safe(String v) {
        return (v == null ? "" : v);
    }
}
