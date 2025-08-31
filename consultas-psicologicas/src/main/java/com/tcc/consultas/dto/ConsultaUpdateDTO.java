// src/main/java/com/tcc/consultas/dto/ConsultaUpdateDTO.java
package com.tcc.consultas.dto;

import java.time.LocalDateTime;

/**
 * Para reagendar/alterar observações e (opcionalmente) status.
 * Caso não queira permitir alteração de status aqui, remova o campo status.
 */
public record ConsultaUpdateDTO(
        LocalDateTime dataHora,
        String observacoes,
        String status // opcional: "AGENDADA", "CANCELADA", "CONCLUIDA"
) {}
