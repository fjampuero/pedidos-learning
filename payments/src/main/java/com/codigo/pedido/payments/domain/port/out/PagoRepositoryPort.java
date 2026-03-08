package com.codigo.pedido.payments.domain.port.out;

import com.codigo.pedido.payments.domain.model.Pago;

import java.util.Optional;
import java.util.UUID;

public interface PagoRepositoryPort {

    Pago guardar(Pago pago);

    Optional<Pago> buscarPorId(UUID id);

    Optional<Pago> buscarPorPedidoId(UUID pedidoId);
}
