package com.tcc.consultas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Para reagendar/alterar observações e (opcional) status.
 */
public record ConsultaUpdateDTO(
        String dataHora,
        String observacoes,
        String status // "AGENDADA", "CANCELADA", "CONCLUIDA" (opcional)
) {}
