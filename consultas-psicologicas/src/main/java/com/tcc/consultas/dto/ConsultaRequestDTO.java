package com.tcc.consultas.dto;

public record ConsultaRequestDTO(
        Long pacienteId,
        Long psicologoId,
        String dataHora,
        String observacoes
) {}