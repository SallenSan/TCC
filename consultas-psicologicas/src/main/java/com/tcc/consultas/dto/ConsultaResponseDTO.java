package com.tcc.consultas.dto;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        String pacienteNome,
        String psicologoNome,
        LocalDateTime dataHora,
        String status,
        String observacoes
) {}
