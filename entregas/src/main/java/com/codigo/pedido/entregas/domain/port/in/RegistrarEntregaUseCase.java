package com.codigo.pedido.entregas.domain.port.in;

import com.codigo.pedido.entregas.domain.model.Entrega;

import java.util.UUID;

public interface RegistrarEntregaUseCase {

    Entrega registrar(UUID pedidoId, UUID usuarioId, String direccionEntrega, String observacion);
}
