package com.codigo.pedido.usuarios.domain.port.out;

import com.codigo.pedido.usuarios.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {
    Optional<Usuario> buscarPorCorreo(String correo);
    Optional<Usuario> buscarPorId(UUID id);
    void guardar(Usuario usuario);
}
