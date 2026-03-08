package com.codigo.pedido.catalogo.domain.port.in;

import com.codigo.pedido.catalogo.domain.model.Producto;

import java.util.UUID;

public interface ObtenerProductoUseCase {

    Producto obtenerPorId(UUID id);
}
