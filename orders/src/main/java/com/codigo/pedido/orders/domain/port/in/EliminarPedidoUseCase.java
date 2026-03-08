package com.codigo.pedido.orders.domain.port.in;

import java.util.UUID;

public interface EliminarPedidoUseCase {

    void eliminar(UUID id);
}
