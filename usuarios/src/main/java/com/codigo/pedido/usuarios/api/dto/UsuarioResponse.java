package com.codigo.pedido.usuarios.api.dto;

import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nombre,
        String correo,
        String rol,
        boolean activo
) {
}
