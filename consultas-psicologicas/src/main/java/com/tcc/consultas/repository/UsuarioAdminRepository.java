package com.tcc.consultas.repository;

import com.tcc.consultas.model.UsuarioAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioAdminRepository extends JpaRepository<UsuarioAdmin, Long> {
    Optional<UsuarioAdmin> findByEmail(String email);
    boolean existsByEmail(String email);
}
