package com.tcc.consultas.service;

import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.repository.PsicologoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PsicologoService {
    private final PsicologoRepository psicologoRepository;

    public PsicologoService(PsicologoRepository psicologoRepository) {
        this.psicologoRepository = psicologoRepository;
    }

    public Psicologo salvar(Psicologo psicologo) {
        return psicologoRepository.save(psicologo);
    }

    public List<Psicologo> listarTodos() {
        return psicologoRepository.findAll();
    }

    public Psicologo buscarPorId(Long id) {
        return psicologoRepository.findById(id).orElseThrow(() -> new RuntimeException("Psicólogo não encontrado"));
    }

    public Psicologo atualizar(Long id, Psicologo psicologo) {
        Psicologo existente = buscarPorId(id);
        existente.setNome(psicologo.getNome());
        existente.setTelefone(psicologo.getTelefone());
        existente.setEspecialidade(psicologo.getEspecialidade());
        existente.setUsuario(psicologo.getUsuario());
        return psicologoRepository.save(existente);
    }

    public void deletar(Long id) {
        psicologoRepository.deleteById(id);
    }
}
