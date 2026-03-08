package com.codigo.pedido.payments.domain.port.out;

import com.codigo.pedido.payments.domain.model.Pago;

public interface PagoEventPublisherPort {

    void publicarPagoAprobado(Pago pago);

    void publicarPagoRechazado(Pago pago);
}
