package com.codigo.pedido.orders.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoItemResponse(
        UUID id,
        UUID productoId,
        String nombreProducto,
        BigDecimal precio,
        int cantidad,
        BigDecimal subtotal
) {
}
