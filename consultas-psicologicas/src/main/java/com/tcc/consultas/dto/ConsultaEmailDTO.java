package com.tcc.consultas.dto;

import java.time.LocalDateTime;

public record ConsultaEmailDTO(
        String nomePaciente,
        String emailPaciente,
        String nomePsicologo,
        String emailPsicologo,
        LocalDateTime dataHora,
        String status
) {}
