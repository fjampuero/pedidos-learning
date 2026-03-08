package com.codigo.pedido.payments.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagoResponse(
        UUID id,
        UUID pedidoId,
        UUID usuarioId,
        BigDecimal monto,
        String estado,
        String metodoPago,
        String referenciaTransaccion,
        LocalDateTime fechaCreacion
) {
}
