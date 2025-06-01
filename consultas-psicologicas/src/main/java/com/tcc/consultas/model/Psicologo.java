package com.tcc.consultas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Psicologo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("nome")
    private String nome;
    @JsonProperty("email")
    private String email;
    @JsonProperty("senha")
    private String senha;
    @JsonProperty("telefone")
    private String telefone;
    @JsonProperty("especialidade")
    private String especialidade;
    @Column(nullable = false, unique = true)
    private String crp; // Registro profissional


    @OneToMany(mappedBy = "psicologo")
    private List<Consulta> consultas;
}
