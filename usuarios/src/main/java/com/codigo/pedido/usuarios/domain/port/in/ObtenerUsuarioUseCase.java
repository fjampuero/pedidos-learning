package com.codigo.pedido.usuarios.domain.port.in;

import com.codigo.pedido.usuarios.domain.model.Usuario;

import java.util.UUID;

public interface ObtenerUsuarioUseCase {

    Usuario obtenerPorId(UUID id);
}
