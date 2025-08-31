package com.tcc.consultas.dto;

public record PsicologoRequestDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String especialidade
) {}
