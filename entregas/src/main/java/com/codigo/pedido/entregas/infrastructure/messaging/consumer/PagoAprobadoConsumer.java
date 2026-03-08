package com.codigo.pedido.entregas.infrastructure.messaging.consumer;

import com.codigo.pedido.entregas.domain.port.in.RegistrarEntregaDesdePagoUseCase;
import com.codigo.pedido.entregas.infrastructure.messaging.event.PagoAprobadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PagoAprobadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PagoAprobadoConsumer.class);

    private final RegistrarEntregaDesdePagoUseCase registrarEntregaDesdePago;

    public PagoAprobadoConsumer(RegistrarEntregaDesdePagoUseCase registrarEntregaDesdePago) {
        this.registrarEntregaDesdePago = registrarEntregaDesdePago;
    }

    @KafkaListener(
            topics = "${events.topics.pago-aprobado}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumirPagoAprobado(PagoAprobadoEvent event) {
        log.info("Evento pago-aprobado recibido para pedido: {} y usuario: {}", event.pedidoId(), event.usuarioId());
        registrarEntregaDesdePago.registrarDesdePago(
                event.pedidoId(),
                event.usuarioId()
        );
    }
}
