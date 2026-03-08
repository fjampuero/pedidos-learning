package com.codigo.pedido.orders.infrastructure.messaging.consumer;

import com.codigo.pedido.orders.domain.port.in.AplicarPagoAprobadoUseCase;
import com.codigo.pedido.orders.infrastructure.messaging.event.PagoAprobadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PagoAprobadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PagoAprobadoConsumer.class);

    private final AplicarPagoAprobadoUseCase aplicarPagoAprobado;

    public PagoAprobadoConsumer(AplicarPagoAprobadoUseCase aplicarPagoAprobado) {
        this.aplicarPagoAprobado = aplicarPagoAprobado;
    }

    @KafkaListener(topics = "${events.topics.pago-aprobado}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumirPagoAprobado(PagoAprobadoEvent event) {
        log.info("Evento pago-aprobado recibido para pedido: {}", event.pedidoId());
        aplicarPagoAprobado.aplicarPagoAprobado(event.pedidoId());
    }
}
