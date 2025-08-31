package com.tcc.consultas.repository;

import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.model.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPaciente(Paciente paciente);
    List<Consulta> findByPsicologo(Psicologo psicologo);
    List<Consulta> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}
