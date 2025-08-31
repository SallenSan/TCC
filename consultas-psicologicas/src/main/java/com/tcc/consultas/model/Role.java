package com.tcc.consultas.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // "ROLE_ADMIN", "ROLE_PSICOLOGO", "ROLE_PACIENTE"
    private String nome;


}
