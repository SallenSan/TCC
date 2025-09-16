package com.tcc.consultas.dto;


/**
 * Para reagendar/alterar observações e (opcional) status.
 */
public record ConsultaUpdateDTO(
        String dataHora,
        String observacoes,
        String status // "AGENDADA", "CANCELADA", "CONCLUIDA"
) {}
