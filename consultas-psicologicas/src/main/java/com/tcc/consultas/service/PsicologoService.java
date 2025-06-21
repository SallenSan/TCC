package com.tcc.consultas.service;

import com.tcc.consultas.dto.PsicologoDTO;
import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.repository.PsicologoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PsicologoService {

    @Autowired
    private PsicologoRepository psicologoRepository;

    public List<Psicologo> listarTodos() {
        return psicologoRepository.findAll();
    }

    public Optional<Psicologo> buscarPorId(Long id) {
        return psicologoRepository.findById(id);
    }

    public boolean existePorId(Long id) {
        return psicologoRepository.existsById(id);
    }

    public Psicologo salvar(PsicologoDTO dto) {
        // Verifica se já existe psicólogo com mesmo CRP
        boolean crpExistente = psicologoRepository.findAll()
                .stream()
                .anyMatch(p -> p.getCrp().equals(dto.getCrp()));

        if (crpExistente) {
            throw new IllegalArgumentException("Já existe um psicólogo com este CRP.");
        }

        Psicologo psicologo = new Psicologo();
        psicologo.setNome(dto.getNome());
        psicologo.setEmail(dto.getEmail());
        psicologo.setSenha(dto.getSenha());
        psicologo.setTelefone(dto.getTelefone());
        psicologo.setEspecialidade(dto.getEspecialidade());
        psicologo.setCrp(dto.getCrp());

        return psicologoRepository.save(psicologo);
    }

    public Psicologo atualizar(Long id, PsicologoDTO dto) {
        Optional<Psicologo> existente = psicologoRepository.findById(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Psicólogo não encontrado com ID: " + id);
        }

        Psicologo psicologo = existente.get();
        psicologo.setNome(dto.getNome());
        psicologo.setEmail(dto.getEmail());
        psicologo.setSenha(dto.getSenha());
        psicologo.setTelefone(dto.getTelefone());
        psicologo.setEspecialidade(dto.getEspecialidade());
        psicologo.setCrp(dto.getCrp());

        return psicologoRepository.save(psicologo);
    }

    public void deletar(Long id) {
        psicologoRepository.deleteById(id);
    }
}
