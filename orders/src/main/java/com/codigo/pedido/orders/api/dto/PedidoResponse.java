package com.codigo.pedido.orders.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
        UUID id,
        UUID usuarioId,
        UUID restauranteId,
        String estado,
        BigDecimal total,
        LocalDateTime fechaCreacion,
        List<PedidoItemResponse> items
) {
}
