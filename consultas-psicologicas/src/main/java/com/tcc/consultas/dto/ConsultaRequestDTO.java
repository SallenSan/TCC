package com.tcc.consultas.dto;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(
        Long pacienteId,
        Long psicologoId,
        LocalDateTime dataHora,
        String observacoes
) {}
