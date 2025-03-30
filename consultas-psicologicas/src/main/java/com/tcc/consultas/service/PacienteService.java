package com.tcc.consultas.service;

import com.tcc.consultas.model.Paciente;
import com.tcc.consultas.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    public Paciente salvar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void deletar(Long id) {
        pacienteRepository.deleteById(id);
    }
    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado) {
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setNome(pacienteAtualizado.getNome());
            paciente.setEmail(pacienteAtualizado.getEmail());
            paciente.setTelefone(pacienteAtualizado.getTelefone());
            paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());
            return pacienteRepository.save(paciente);
        }).orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }
}
