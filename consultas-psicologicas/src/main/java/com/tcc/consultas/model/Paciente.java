package com.tcc.consultas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paciente {

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("dataNascimento")
    private LocalDate dataNascimento;

    @JsonIgnore
    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;
}
