package com.tcc.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Email
    @Column(name = "email", unique = true)
    private String email;

    private String telefone;
    private String crp;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private UsuarioAdmin usuario;

    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL)
    private List<Consulta> consultas;


}
