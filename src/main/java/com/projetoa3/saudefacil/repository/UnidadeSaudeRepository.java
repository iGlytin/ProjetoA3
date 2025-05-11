package com.projetoa3.saudefacil.repository;

import com.projetoa3.saudefacil.entities.UnidadeSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {
    List<UnidadeSaude> findByNomeContaining(String nome);
}
