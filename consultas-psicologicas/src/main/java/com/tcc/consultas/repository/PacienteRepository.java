package com.tcc.consultas.repository;

import com.tcc.consultas.model.Paciente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Override
    @EntityGraph(attributePaths = "usuario")
    List<Paciente> findAll();
}
