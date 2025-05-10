package com.projetoa3.saudefacil.repository;

import com.projetoa3.saudefacil.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
