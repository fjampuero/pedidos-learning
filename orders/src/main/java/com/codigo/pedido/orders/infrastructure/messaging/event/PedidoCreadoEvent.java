package com.codigo.pedido.orders.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoCreadoEvent(
        UUID pedidoId,
        UUID usuarioId,
        UUID restauranteId,
        String estado,
        BigDecimal total,
        LocalDateTime fechaCreacion,
        List<PedidoCreadoItemEvent> items
) {
}
