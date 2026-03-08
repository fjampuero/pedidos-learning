package com.codigo.pedido.catalogo.domain.port.out;

import com.codigo.pedido.catalogo.domain.model.Producto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoRepositoryPort {

    Producto guardar(Producto producto);

    List<Producto> listarPorRestaurante(UUID restauranteId);

    Optional<Producto> buscarPorId(UUID id);
}
