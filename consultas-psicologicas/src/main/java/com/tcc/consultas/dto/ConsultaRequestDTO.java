// ConsultaRequestDTO.java
package com.tcc.consultas.dto;

import java.time.OffsetDateTime;

public record ConsultaRequestDTO(
        Long pacienteId,
        Long psicologoId,
        String dataHora,
        String observacoes
) {}
