package com.codigo.pedido.entregas.infrastructure.messaging.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaCreadaEvent(
        UUID entregaId,
        UUID pedidoId,
        UUID usuarioId,
        String direccionEntrega,
        String estado,
        String observacion,
        LocalDateTime fechaCreacion
) {
}
