package com.codigo.pedido.payments.domain.port.in;

import com.codigo.pedido.payments.domain.model.Pago;

import java.util.UUID;

public interface ObtenerPagoPorPedidoUseCase {

    Pago obtenerPorPedidoId(UUID pedidoId);
}
