package com.codigo.pedido.entregas.domain.port.out;

import com.codigo.pedido.entregas.domain.model.Entrega;

import java.util.Optional;
import java.util.UUID;

public interface EntregaRepositoryPort {

    Entrega guardar(Entrega entrega);

    Optional<Entrega> buscarPorId(UUID id);

    Optional<Entrega> buscarPorPedidoId(UUID pedidoId);
}
