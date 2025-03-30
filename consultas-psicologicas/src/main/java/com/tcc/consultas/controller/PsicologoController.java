package com.tcc.consultas.controller;

import com.tcc.consultas.model.Psicologo;
import com.tcc.consultas.service.PsicologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/psicologos")
public class PsicologoController {

    @Autowired
    private PsicologoService psicologoService;

    @GetMapping
    public List<Psicologo> listarTodos() {
        return psicologoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Psicologo> buscarPorId(@PathVariable Long id) {
        return psicologoService.buscarPorId(id);
    }

    @PostMapping
    public Psicologo salvar(@RequestBody Psicologo psicologo) {
        return psicologoService.salvar(psicologo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        psicologoService.deletar(id);
    }

    @PutMapping("/{id}")
    public Psicologo atualizarPsicologo(@PathVariable Long id, @RequestBody Psicologo psicologoAtualizado) {
        return psicologoService.atualizarPsicologo(id, psicologoAtualizado);
    }
}
