package com.codigo.pedido.orders.domain.port.in;

import com.codigo.pedido.orders.domain.model.Pedido;

import java.util.List;
import java.util.UUID;

public interface ListarPedidosUseCase {

    List<Pedido> listar(UUID usuarioId, String estado);
}
