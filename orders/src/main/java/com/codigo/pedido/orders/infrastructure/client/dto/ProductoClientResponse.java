package com.codigo.pedido.orders.infrastructure.client.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoClientResponse(
        UUID id,
        UUID restauranteId,
        String nombre,
        String descripcion,
        BigDecimal precio,
        boolean disponible
) {
}
