package com.codigo.pedido.orders.domain.port.in;

import com.codigo.pedido.orders.domain.model.Pedido;

import java.util.UUID;

public interface AplicarPagoAprobadoUseCase {

    Pedido aplicarPagoAprobado(UUID pedidoId);
}
