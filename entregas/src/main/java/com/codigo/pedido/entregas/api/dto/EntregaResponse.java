package com.codigo.pedido.entregas.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaResponse(
        UUID id,
        UUID pedidoId,
        UUID usuarioId,
        String direccionEntrega,
        String repartidor,
        String estado,
        String observacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaEntrega
) {
}
