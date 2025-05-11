package com.projetoa3.saudefacil.controllers;

import com.projetoa3.saudefacil.entities.Agendamento;
import com.projetoa3.saudefacil.entities.UnidadeSaude;
import com.projetoa3.saudefacil.entities.Usuario;
import com.projetoa3.saudefacil.repository.AgendamentoRepository;
import com.projetoa3.saudefacil.repository.UnidadeSaudeRepository;
import com.projetoa3.saudefacil.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;

    @Autowired
    public AgendamentoController(AgendamentoRepository agendamentoRepository,
                                 UsuarioRepository usuarioRepository,
                                 UnidadeSaudeRepository unidadeSaudeRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.unidadeSaudeRepository = unidadeSaudeRepository;
    }

    // Cria novo agendamento
    @PostMapping
    public ResponseEntity<?> criarAgendamento(@Valid @RequestBody Agendamento agendamento) {
        try {
            // Valida relacionamentos
            if (!validarRelacionamentos(agendamento)) {
                return ResponseEntity.badRequest().body("Paciente, profissional ou unidade não encontrados");
            }

            // Valida conflito de horário
            if (existeConflitoAgendamento(agendamento)) {
                return ResponseEntity.badRequest().body("Já existe um agendamento para este profissional no horário selecionado");
            }

            Agendamento novoAgendamento = agendamentoRepository.save(agendamento);
            return new ResponseEntity<>(novoAgendamento, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lista todos agendamentos
    @GetMapping
    public ResponseEntity<List<Agendamento>> listarAgendamentos() {
        List<Agendamento> agendamentos = agendamentoRepository.findAll();
        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }

    // Busca agendamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarAgendamentoPorId(@PathVariable Long id) {
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        return agendamento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Atualiza agendamento
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAgendamento(@PathVariable Long id, @Valid @RequestBody Agendamento agendamentoAtualizado) {
        Optional<Agendamento> agendamentoData = agendamentoRepository.findById(id);

        if (agendamentoData.isPresent()) {
            Agendamento agendamento = agendamentoData.get();

            // Valida relacionamentos
            if (!validarRelacionamentos(agendamentoAtualizado)) {
                return ResponseEntity.badRequest().body("Paciente, profissional ou unidade não encontrados");
            }

            // Atualiza campos
            agendamento.setPaciente(agendamentoAtualizado.getPaciente());
            agendamento.setProfissional(agendamentoAtualizado.getProfissional());
            agendamento.setUnidade(agendamentoAtualizado.getUnidade());
            agendamento.setDataHora(agendamentoAtualizado.getDataHora());
            agendamento.setStatus(agendamentoAtualizado.getStatus());
            agendamento.setObservacoes(agendamentoAtualizado.getObservacoes());

            return new ResponseEntity<>(agendamentoRepository.save(agendamento), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Deleta agendamento
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletarAgendamento(@PathVariable Long id) {
        try {
            agendamentoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoints adicionais

    // Busca agendamentos por paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Agendamento>> buscarPorPaciente(@PathVariable Long pacienteId) {
        List<Agendamento> agendamentos = agendamentoRepository.findByPacienteId(pacienteId);
        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }

    // Busca agendamentos por profissional
    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<List<Agendamento>> buscarPorProfissional(@PathVariable Long profissionalId) {
        List<Agendamento> agendamentos = agendamentoRepository.findByProfissionalId(profissionalId);
        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }

    // Busca agendamentos por período
    @GetMapping("/periodo")
    public ResponseEntity<List<Agendamento>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<Agendamento> agendamentos = agendamentoRepository.findByDataHoraBetween(inicio, fim);
        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }

    // Métodos auxiliares
    private boolean validarRelacionamentos(Agendamento agendamento) {
        return usuarioRepository.existsById(agendamento.getPaciente().getId()) &&
                usuarioRepository.existsById(agendamento.getProfissional().getId()) &&
                unidadeSaudeRepository.existsById(agendamento.getUnidade().getId());
    }

    private boolean existeConflitoAgendamento(Agendamento agendamento) {
        return agendamentoRepository.existsByProfissionalAndDataHora(
                agendamento.getProfissional(),
                agendamento.getDataHora());
    }
}