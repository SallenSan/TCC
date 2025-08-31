package com.tcc.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "psicologos")
public class Psicologo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String especialidade;

    private String telefone;
    private String crp;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL)
    private List<Consulta> consultas;


}
