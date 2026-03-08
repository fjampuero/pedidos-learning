package com.codigo.pedido.payments.domain.port.in;

import com.codigo.pedido.payments.domain.model.Pago;

import java.math.BigDecimal;
import java.util.UUID;

public interface RegistrarPagoUseCase {

    Pago registrar(UUID pedidoId, UUID usuarioId, BigDecimal monto, String metodoPago);
}
