package com.codigo.pedido.entregas.domain.port.out;

import com.codigo.pedido.entregas.domain.model.Entrega;

public interface EntregaEventPublisherPort {

    void publicarEntregaCreada(Entrega entrega);

    void publicarEntregaActualizada(Entrega entrega);
}
