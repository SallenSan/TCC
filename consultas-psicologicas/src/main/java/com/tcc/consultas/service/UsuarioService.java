package com.tcc.consultas.service;

import com.tcc.consultas.model.Usuario;
import com.tcc.consultas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(existente -> {
                    existente.setNome(usuario.getNome());
                    existente.setEmail(usuario.getEmail());
                    existente.setSenhaHash(usuario.getSenhaHash());
                    existente.setRoles(usuario.getRoles());
                    return usuarioRepository.save(existente);
                }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
