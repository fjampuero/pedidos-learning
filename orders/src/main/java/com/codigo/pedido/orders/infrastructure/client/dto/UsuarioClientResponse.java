package com.codigo.pedido.orders.infrastructure.client.dto;

import java.util.UUID;

public record UsuarioClientResponse(
        UUID id,
        String nombre,
        String correo,
        String rol,
        boolean activo
) {
}
