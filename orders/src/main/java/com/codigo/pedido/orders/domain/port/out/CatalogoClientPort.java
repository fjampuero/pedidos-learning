package com.codigo.pedido.orders.domain.port.out;

import java.util.UUID;

public interface CatalogoClientPort {

    ProductoCatalogo obtenerProducto(UUID productoId);
}
