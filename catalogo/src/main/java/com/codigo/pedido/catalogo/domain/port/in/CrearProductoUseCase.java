package com.codigo.pedido.catalogo.domain.port.in;

import com.codigo.pedido.catalogo.domain.model.Producto;

public interface CrearProductoUseCase {

    Producto crear(Producto producto);
}