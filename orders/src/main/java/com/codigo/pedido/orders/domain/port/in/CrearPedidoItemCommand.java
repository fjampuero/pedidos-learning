package com.codigo.pedido.orders.domain.port.in;

import java.util.UUID;

public record CrearPedidoItemCommand(
        UUID productoId,
        int cantidad
) {
}
