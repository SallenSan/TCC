package com.tcc.consultas.dto;

public record PsicologoResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String especialidade
) {}
