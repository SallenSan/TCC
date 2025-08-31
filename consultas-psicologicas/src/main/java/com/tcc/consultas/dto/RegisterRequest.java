package com.tcc.consultas.dto;

public record RegisterRequest(
        String nome,
        String email,
        String senha,
        String role // Pode ser "PACIENTE" ou "PSICOLOGO"
) {}
