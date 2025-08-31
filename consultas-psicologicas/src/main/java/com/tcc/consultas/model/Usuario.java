package com.tcc.consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String nome;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String senhaHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // Ligação opcional com perfis:
    @OneToOne(mappedBy = "usuario")
    private Paciente paciente;

    @OneToOne(mappedBy = "usuario")
    private Psicologo psicologo;


}
