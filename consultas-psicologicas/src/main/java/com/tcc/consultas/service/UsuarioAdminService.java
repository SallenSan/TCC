package com.tcc.consultas.service;

import com.tcc.consultas.model.UsuarioAdmin;
import com.tcc.consultas.repository.UsuarioAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioAdminService {

    @Autowired
    private UsuarioAdminRepository usuarioAdminRepository;

    // Listar todos os admins
    public List<UsuarioAdmin> listarTodos() {
        return usuarioAdminRepository.findAll();
    }

    // Buscar admin por ID
    public Optional<UsuarioAdmin> buscarPorId(Long id) {
        return usuarioAdminRepository.findById(id);
    }

    // Buscar admin por email (útil para login)
    public Optional<UsuarioAdmin> buscarPorEmail(String email) {
        return usuarioAdminRepository.findByEmail(email);
    }

    // Salvar novo admin
    public UsuarioAdmin salvar(UsuarioAdmin usuario) {
        return usuarioAdminRepository.save(usuario);
    }

    // Atualizar admin existente
    public UsuarioAdmin atualizar(Long id, UsuarioAdmin usuario) {
        return usuarioAdminRepository.findById(id)
                .map(existente -> {
                    existente.setNome(usuario.getNome());
                    existente.setEmail(usuario.getEmail());
                    existente.setSenhaHash(usuario.getSenhaHash());
                    return usuarioAdminRepository.save(existente);
                }).orElseThrow(() -> new RuntimeException("Usuário Admin não encontrado"));
    }

    // Deletar admin
    public void deletar(Long id) {
        usuarioAdminRepository.deleteById(id);
    }
}
