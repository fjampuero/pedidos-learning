package com.codigo.pedido.orders.domain.port.out;

import java.util.UUID;

public interface UsuarioClientPort {

    void validarUsuario(UUID usuarioId);
}
