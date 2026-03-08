package com.codigo.pedido.orders.domain.port.out;

import com.codigo.pedido.orders.domain.model.Pedido;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepositoryPort {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(UUID id);

    List<Pedido> listarTodos();

    List<Pedido> listarPorUsuario(UUID usuarioId);

    List<Pedido> listarPorEstado(String estado);

    void eliminar(UUID id);
}
