package com.projetoa3.saudefacil.controllers;

import com.projetoa3.saudefacil.entities.UnidadeSaude;
import com.projetoa3.saudefacil.repository.UnidadeSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/unidades-saude")
public class UnidadeSaudeController {

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    @Autowired
    public UnidadeSaudeController(UnidadeSaudeRepository unidadeSaudeRepository) {
        this.unidadeSaudeRepository = unidadeSaudeRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<UnidadeSaude> criarUnidadeSaude(@RequestBody UnidadeSaude unidadeSaude) {
        try {
            UnidadeSaude novaUnidade = unidadeSaudeRepository.save(unidadeSaude);
            return new ResponseEntity<>(novaUnidade, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<UnidadeSaude>> listarTodasUnidades() {
        List<UnidadeSaude> unidades = unidadeSaudeRepository.findAll();
        return new ResponseEntity<>(unidades, HttpStatus.OK);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeSaude> buscarUnidadePorId(@PathVariable Long id) {
        Optional<UnidadeSaude> unidadeData = unidadeSaudeRepository.findById(id);

        return unidadeData.map(unidade -> new ResponseEntity<>(unidade, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeSaude> atualizarUnidade(
            @PathVariable Long id, @RequestBody UnidadeSaude unidadeSaude) {
        Optional<UnidadeSaude> unidadeData = unidadeSaudeRepository.findById(id);

        if (unidadeData.isPresent()) {
            UnidadeSaude unidade = unidadeData.get();
            unidade.setNome(unidadeSaude.getNome());
            unidade.setEndereco(unidadeSaude.getEndereco());
            unidade.setTelefone(unidadeSaude.getTelefone());
            return new ResponseEntity<>(unidadeSaudeRepository.save(unidade), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletarUnidade(@PathVariable Long id) {
        try {
            unidadeSaudeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // SEARCH BY NAME (exemplo de endpoint adicional)
    @GetMapping("/buscar")
    public ResponseEntity<List<UnidadeSaude>> buscarPorNome(@RequestParam String nome) {
        try {
            List<UnidadeSaude> unidades = unidadeSaudeRepository.findByNomeContaining(nome);

            if (unidades.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(unidades, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
