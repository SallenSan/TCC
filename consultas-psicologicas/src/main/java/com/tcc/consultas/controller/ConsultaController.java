package com.tcc.consultas.controller;

import com.tcc.consultas.model.Consulta;
import com.tcc.consultas.service.ConsultaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public Consulta criar(@RequestBody Consulta consulta) {
        return consultaService.salvar(consulta);
    }

    @GetMapping
    public List<Consulta> listar() {
        return consultaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Consulta buscar(@PathVariable Long id) {
        return consultaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Consulta atualizar(@PathVariable Long id, @RequestBody Consulta consulta) {
        return consultaService.atualizar(id, consulta);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        consultaService.deletar(id);
    }
}
