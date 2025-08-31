
package com.tcc.consultas.dto;

import java.time.LocalDateTime;

public record ConsultaCreateDTO(
        Long pacienteId,
        Long psicologoId,
        LocalDateTime dataHora,
        String observacoes
) {}
