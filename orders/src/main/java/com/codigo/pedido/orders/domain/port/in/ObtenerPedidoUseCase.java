package com.codigo.pedido.orders.domain.port.in;

import com.codigo.pedido.orders.domain.model.Pedido;

import java.util.UUID;

public interface ObtenerPedidoUseCase {

    Pedido obtenerPorId(UUID id);
}
