package com.codigo.pedido.catalogo.domain.port.in;

import com.codigo.pedido.catalogo.domain.model.Producto;

import java.util.List;
import java.util.UUID;

public interface ListarProductosPorRestauranteUseCase {

    List<Producto> listar(UUID restauranteId);
}