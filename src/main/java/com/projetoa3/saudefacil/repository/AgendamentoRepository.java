package com.projetoa3.saudefacil.repository;

import com.projetoa3.saudefacil.entities.Agendamento;
import com.projetoa3.saudefacil.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByPacienteId(Long pacienteId);

    List<Agendamento> findByProfissionalId(Long profissionalId);

    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    boolean existsByProfissionalAndDataHora(Usuario profissional, LocalDateTime dataHora);

    // Consulta personalizada exemplo
    @Query("SELECT a FROM Agendamento a WHERE a.unidade.id = :unidadeId AND a.status = :status")
    List<Agendamento> findByUnidadeAndStatus(@Param("unidadeId") Long unidadeId, @Param("status") String status);
}