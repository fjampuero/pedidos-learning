package com.codigo.pedido.orders.domain.port.in;

import com.codigo.pedido.orders.domain.model.Pedido;

import java.util.UUID;

public interface ActualizarEstadoPedidoUseCase {

    Pedido actualizarEstado(UUID id, String estado);
}
