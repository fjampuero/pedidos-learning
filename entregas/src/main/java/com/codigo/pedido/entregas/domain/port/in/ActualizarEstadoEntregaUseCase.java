package com.codigo.pedido.entregas.domain.port.in;

import com.codigo.pedido.entregas.domain.model.Entrega;

import java.util.UUID;

public interface ActualizarEstadoEntregaUseCase {

    Entrega actualizarEstado(UUID id, String estado);
}
