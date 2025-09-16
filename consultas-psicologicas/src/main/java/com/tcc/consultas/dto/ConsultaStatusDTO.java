package com.tcc.consultas.dto;

import jakarta.validation.constraints.NotBlank;

public record ConsultaStatusDTO(
        @NotBlank String status
) {}
