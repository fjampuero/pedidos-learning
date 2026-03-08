package com.codigo.pedido.entregas.domain.port.in;

import com.codigo.pedido.entregas.domain.model.Entrega;

import java.util.UUID;

public interface RegistrarEntregaDesdePagoUseCase {

    Entrega registrarDesdePago(UUID pedidoId, UUID usuarioId);
}
