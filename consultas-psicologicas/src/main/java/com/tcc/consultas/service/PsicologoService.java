package com.tcc.consultas.service;


import com.tcc.consultas.model.Paciente;
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

    public Psicologo salvar(Psicologo psicologo) {
        return psicologoRepository.save(psicologo);
    }

    public void deletar(Long id) {
        psicologoRepository.deleteById(id);
    }

    public Psicologo atualizarPsicologo(Long id, Psicologo psicologoAtualizado) {
        return psicologoRepository.findById(id).map(psicologo -> {
            psicologo.setNome(psicologoAtualizado.getNome());
            psicologo.setEmail(psicologoAtualizado.getEmail());
            psicologo.setTelefone(psicologoAtualizado.getTelefone());
            psicologo.setEspecialidade(psicologoAtualizado.getEspecialidade());
            return psicologoRepository.save(psicologo);
        }).orElseThrow(() -> new RuntimeException("Psicólogo não encontrado!"));
    }
}

