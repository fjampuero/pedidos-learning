package com.codigo.pedido.entregas.infrastructure.messaging.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaActualizadaEvent(
        UUID entregaId,
        UUID pedidoId,
        UUID usuarioId,
        String estado,
        LocalDateTime fechaEntrega
) {
}
