package com.codigo.pedido.orders.domain.port.out;

import com.codigo.pedido.orders.domain.model.Pedido;

public interface PedidoEventPublisherPort {

    void publicarPedidoCreado(Pedido pedido);
}
