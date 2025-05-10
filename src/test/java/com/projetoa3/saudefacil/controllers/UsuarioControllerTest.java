package com.projetoa3.saudefacil.controllers;

import com.projetoa3.saudefacil.entities.Usuario;
import com.projetoa3.saudefacil.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void criarUsuario_DeveRetornarUsuarioSalvo() {
        // Arrange (Preparação)
        Usuario usuario = new Usuario();
        usuario.setNome("João");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act (Ação)
        ResponseEntity<Usuario> response = usuarioController.criarUsuario(usuario);

        // Assert (Verificação)
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo");
        assertEquals("João", response.getBody().getNome());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void buscarPorId_DeveRetornarUsuario() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        ResponseEntity<Usuario> response = usuarioController.buscarUsuarioPorId(1L);

        // Assert
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo");
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1L, response.getBody().getId());
    }
}