package com.codigo.pedido.orders.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoCreadoItemEvent(
        UUID productoId,
        String nombreProducto,
        BigDecimal precio,
        int cantidad,
        BigDecimal subtotal
) {
}
