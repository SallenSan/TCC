// ConsultaRequestDTO.java
package com.tcc.consultas.dto;

import java.time.OffsetDateTime;

public record ConsultaRequestDTO(
        Long pacienteId,
        Long psicologoId,
        OffsetDateTime dataHora,
        String observacoes
) {}
