package com.tcc.consultas.dto;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone
) {}
