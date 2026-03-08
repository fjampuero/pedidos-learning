package com.codigo.pedido.entregas.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagoAprobadoEvent(
        UUID pagoId,
        UUID pedidoId,
        UUID usuarioId,
        BigDecimal monto,
        String estado,
        String metodoPago,
        String referenciaTransaccion,
        LocalDateTime fechaCreacion
) {
}
