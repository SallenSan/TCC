package com.tcc.consultas.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String dataNascimento;

    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;
}
