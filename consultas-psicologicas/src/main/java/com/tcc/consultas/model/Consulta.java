package com.tcc.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consultas")
public class Consulta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FutureOrPresent @NotNull
    private LocalDateTime dataHora;

    @ManyToOne @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne @JoinColumn(name = "psicologo_id", nullable = false)
    private Psicologo psicologo;

    @Enumerated(EnumType.STRING)
    private StatusConsulta status = StatusConsulta.AGENDADA;

    private String observacoes;

    public enum StatusConsulta { AGENDADA, CONFIRMADA, REMARCADA, CANCELADA, CONCLUIDA }


}
