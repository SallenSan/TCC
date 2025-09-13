package com.tcc.consultas.repository;

import com.tcc.consultas.model.Psicologo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsicologoRepository extends JpaRepository<Psicologo, Long> {

    @Override
    @EntityGraph(attributePaths = "usuario")
    List<Psicologo> findAll();
}
