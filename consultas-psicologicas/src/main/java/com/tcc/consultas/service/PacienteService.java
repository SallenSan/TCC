package com.tcc.consultas.service;

import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente salvar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    public Paciente atualizar(Long id, Paciente paciente) {
        Paciente existente = buscarPorId(id);
        existente.setNome(paciente.getNome());
        existente.setTelefone(paciente.getTelefone());
        existente.setUsuario(paciente.getUsuario());
        return pacienteRepository.save(existente);
    }

    public void deletar(Long id) {
        pacienteRepository.deleteById(id);
    }
}
