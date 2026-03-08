package com.codigo.pedido.orders.domain.port.out;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductoCatalogo(
        UUID id,
        UUID restauranteId,
        String nombre,
        BigDecimal precio,
        boolean disponible
) {
}
